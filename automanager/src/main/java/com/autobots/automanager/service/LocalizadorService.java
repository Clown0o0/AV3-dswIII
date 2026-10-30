package com.autobots.automanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.exception.RecursoNaoEncontradoException;
import com.autobots.automanager.repositorios.CredencialRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EmailRepositorio;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Service
public class LocalizadorService {

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;

    @Autowired
    private ServicoRepositorio servicoRepositorio;

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @Autowired
    private TelefoneRepositorio telefoneRepositorio;

    @Autowired
    private EnderecoRepositorio enderecoRepositorio;

    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    @Autowired
    private EmailRepositorio emailRepositorio;

    @Autowired
    private CredencialRepositorio credencialRepositorio;

    public Empresa empresa(Long id) {
        return buscar(empresaRepositorio, id, "Nenhuma empresa encontrada com o id ");
    }

    public Usuario usuario(Long id) {
        return buscar(usuarioRepositorio, id, "Nenhum usuário encontrado com o id ");
    }

    public Veiculo veiculo(Long id) {
        return buscar(veiculoRepositorio, id, "Nenhum veículo encontrado com o id ");
    }

    public Mercadoria mercadoria(Long id) {
        return buscar(mercadoriaRepositorio, id, "Nenhuma mercadoria encontrada com o id ");
    }

    public Servico servico(Long id) {
        return buscar(servicoRepositorio, id, "Nenhum serviço encontrado com o id ");
    }

    public Venda venda(Long id) {
        return buscar(vendaRepositorio, id, "Nenhuma venda encontrada com o id ");
    }

    public Telefone telefone(Long id) {
        return buscar(telefoneRepositorio, id, "Nenhum telefone encontrado com o id ");
    }

    public Endereco endereco(Long id) {
        return buscar(enderecoRepositorio, id, "Nenhum endereço encontrado com o id ");
    }

    public Documento documento(Long id) {
        return buscar(documentoRepositorio, id, "Nenhum documento encontrado com o id ");
    }

    public Email email(Long id) {
        return buscar(emailRepositorio, id, "Nenhum e-mail encontrado com o id ");
    }

    public Credencial credencial(Long id) {
        return buscar(credencialRepositorio, id, "Nenhuma credencial encontrada com o id ");
    }

    private <T> T buscar(JpaRepository<T, Long> repositorio, Long id, String mensagem) {
        return repositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(mensagem + id));
    }
}
