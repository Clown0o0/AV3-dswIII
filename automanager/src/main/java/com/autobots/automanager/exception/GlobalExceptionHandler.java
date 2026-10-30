package com.autobots.automanager.exception;

import java.util.Map;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.autobots.automanager.dto.ErroRespostaDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DADOS_INVALIDOS = "Dados inválidos";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new TreeMap<>();
        ex.getBindingResult()
          .getFieldErrors()
          .forEach(campo -> campos.putIfAbsent(campo.getField(), campo.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErroRespostaDTO(DADOS_INVALIDOS, 400, campos));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroRespostaDTO> tratarValidacaoServico(ConstraintViolationException ex) {
        Map<String, String> campos = new TreeMap<>();
        for (ConstraintViolation<?> violacao : ex.getConstraintViolations()) {
            campos.putIfAbsent(campo(violacao.getPropertyPath().toString()), violacao.getMessage());
        }
        return ResponseEntity.badRequest().body(new ErroRespostaDTO(DADOS_INVALIDOS, 400, campos));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return montarResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRegraNegocio(RegraNegocioException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRespostaDTO> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroRespostaDTO> tratarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, "Valor inválido para o parâmetro '" + ex.getName() + "'");
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroRespostaDTO> tratarConflitoNegocio(ConflitoException ex) {
        return montarResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroRespostaDTO> tratarConflito(DataIntegrityViolationException ex) {
        return montarResposta(HttpStatus.CONFLICT, "Operação viola a integridade dos dados: registro duplicado ou referenciado por outro");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRuntime(RuntimeException ex) {
        return montarResposta(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErroRespostaDTO> montarResposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroRespostaDTO(mensagem, status.value()));
    }

    private String campo(String caminho) {
        String[] partes = caminho.split("\\.", 3);
        return partes.length == 3 ? partes[2] : partes[partes.length - 1];
    }
}
