package com.autobots.automanager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.CredencialUsuarioSenhaDTO;
import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.dto.ServicoDTO;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.dto.UsuarioDTO;
import com.autobots.automanager.dto.VeiculoDTO;
import com.autobots.automanager.dto.VendaDTO;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import com.autobots.automanager.service.EmpresaService;
import com.autobots.automanager.service.MercadoriaService;
import com.autobots.automanager.service.ServicoService;
import com.autobots.automanager.service.UsuarioService;
import com.autobots.automanager.service.VeiculoService;
import com.autobots.automanager.service.VendaService;

@SpringBootApplication
public class AutomanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    @Component
    public static class Runner implements ApplicationRunner {

        @Autowired
        private EmpresaService empresaService;

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private VeiculoService veiculoService;

        @Autowired
        private MercadoriaService mercadoriaService;

        @Autowired
        private ServicoService servicoService;

        @Autowired
        private VendaService vendaService;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            EmpresaDTO empresa = new EmpresaDTO();
            empresa.setRazaoSocial("AutoBots Comércio de Autopeças Ltda");
            empresa.setNomeFantasia("AutoBots São José");
            empresa.setTelefones(List.of(telefone("12", "3941-2000")));
            empresa.setEndereco(endereco("São Paulo", "São José dos Campos", "Centro", "Rua Sete de Setembro", "250", "12210-000", "Loja e oficina"));
            Long empresaId = empresaService.criar(empresa).getId();

            UsuarioDTO funcionario = new UsuarioDTO();
            funcionario.setNome("Carla Mendes");
            funcionario.setPerfis(Set.of(PerfilUsuario.FUNCIONARIO));
            funcionario.setEmpresaId(empresaId);
            funcionario.setDocumentos(List.of(documento(TipoDocumento.CPF, "11144477735", data(2010, Calendar.MARCH, 2))));
            funcionario.setEmails(List.of(email("carla.mendes@autobots.com.br")));
            funcionario.setCredenciais(List.of(credencial("carla.mendes", "oficina2026")));
            Long funcionarioId = usuarioService.criar(funcionario).getId();

            UsuarioDTO cliente = new UsuarioDTO();
            cliente.setNome("Pedro Alcântara de Bragança e Bourbon");
            cliente.setNomeSocial("Dom Pedro");
            cliente.setPerfis(Set.of(PerfilUsuario.CLIENTE));
            cliente.setEmpresaId(empresaId);
            cliente.setTelefones(List.of(telefone("21", "98123-4576")));
            cliente.setEndereco(endereco("Rio de Janeiro", "Rio de Janeiro", "Copacabana", "Avenida Atlântica", "1702", "22021-001", "Hotel Copacabana Palace"));
            cliente.setDocumentos(List.of(
                    documento(TipoDocumento.CPF, "52998224725", data(2002, Calendar.JUNE, 15)),
                    documento(TipoDocumento.RG, "12.345.678-9", data(2002, Calendar.JUNE, 15))));
            cliente.setEmails(List.of(email("dom.pedro@imperio.com.br")));
            cliente.setCredenciais(List.of(credencial("dom.pedro", "imperador1822")));
            Long clienteId = usuarioService.criar(cliente).getId();

            VeiculoDTO veiculo = new VeiculoDTO();
            veiculo.setTipo(TipoVeiculo.SUV);
            veiculo.setModelo("Jeep Compass");
            veiculo.setPlaca("BRA2E19");
            veiculo.setProprietarioId(clienteId);
            Long veiculoId = veiculoService.criar(veiculo).getId();

            MercadoriaDTO mercadoria = new MercadoriaDTO();
            mercadoria.setNome("Pastilha de freio dianteira");
            mercadoria.setDescricao("Jogo com 4 pastilhas cerâmicas");
            mercadoria.setFabricao(data(2026, Calendar.JANUARY, 15));
            mercadoria.setValidade(data(2031, Calendar.JANUARY, 15));
            mercadoria.setQuantidade(40L);
            mercadoria.setValor(189.9);
            mercadoria.setEmpresaId(empresaId);
            Long mercadoriaId = mercadoriaService.criar(mercadoria).getId();

            ServicoDTO servico = new ServicoDTO();
            servico.setNome("Troca de pastilhas de freio");
            servico.setDescricao("Mão de obra para substituição das pastilhas dianteiras");
            servico.setValor(120.0);
            servico.setEmpresaId(empresaId);
            Long servicoId = servicoService.criar(servico).getId();

            VendaDTO venda = new VendaDTO();
            venda.setIdentificacao("VND-2026-0001");
            venda.setEmpresaId(empresaId);
            venda.setClienteId(clienteId);
            venda.setFuncionarioId(funcionarioId);
            venda.setVeiculoId(veiculoId);
            venda.setMercadorias(List.of(mercadoriaId));
            venda.setServicos(List.of(servicoId));
            vendaService.criar(venda);
        }

        private Date data(int ano, int mes, int dia) {
            Calendar calendario = Calendar.getInstance();
            calendario.clear();
            calendario.set(ano, mes, dia);
            return calendario.getTime();
        }

        private TelefoneDTO telefone(String ddd, String numero) {
            TelefoneDTO telefone = new TelefoneDTO();
            telefone.setDdd(ddd);
            telefone.setNumero(numero);
            return telefone;
        }

        private EnderecoDTO endereco(String estado, String cidade, String bairro, String rua, String numero, String codigoPostal,
                String informacoesAdicionais) {
            EnderecoDTO endereco = new EnderecoDTO();
            endereco.setEstado(estado);
            endereco.setCidade(cidade);
            endereco.setBairro(bairro);
            endereco.setRua(rua);
            endereco.setNumero(numero);
            endereco.setCodigoPostal(codigoPostal);
            endereco.setInformacoesAdicionais(informacoesAdicionais);
            return endereco;
        }

        private DocumentoDTO documento(TipoDocumento tipo, String numero, Date dataEmissao) {
            DocumentoDTO documento = new DocumentoDTO();
            documento.setTipo(tipo);
            documento.setNumero(numero);
            documento.setDataEmissao(dataEmissao);
            return documento;
        }

        private EmailDTO email(String endereco) {
            EmailDTO email = new EmailDTO();
            email.setEndereco(endereco);
            return email;
        }

        private CredencialUsuarioSenhaDTO credencial(String nomeUsuario, String senha) {
            CredencialUsuarioSenhaDTO credencial = new CredencialUsuarioSenhaDTO();
            credencial.setNomeUsuario(nomeUsuario);
            credencial.setSenha(senha);
            return credencial;
        }
    }
}
