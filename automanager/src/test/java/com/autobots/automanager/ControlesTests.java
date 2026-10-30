package com.autobots.automanager;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:automanager;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=" })
class ControlesTests {

    private static final AtomicInteger SEQUENCIA = new AtomicInteger(100);

    @Autowired
    private MockMvc mvc;

    private static class Cenario {
        String empresa;
        String funcionario;
        String cliente;
        String veiculo;
        String mercadoria;
        String servico;
        String venda;
    }

    private ResultActions enviarPost(String rota, String corpo) throws Exception {
        return mvc.perform(post(rota).contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON).content(corpo));
    }

    private ResultActions enviarPut(String rota, String corpo) throws Exception {
        return mvc.perform(put(rota).contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON).content(corpo));
    }

    private String criar(String rota, String corpo, String recurso) throws Exception {
        String location = enviarPost(rota, corpo)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/" + recurso + "/")))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andReturn().getResponse().getHeader("Location");
        return URI.create(location).getPath();
    }

    private String idDe(String location) {
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private int numero() {
        return SEQUENCIA.incrementAndGet();
    }

    private String cpf() {
        String base = String.format("%09d", 100000000 + numero());
        int[] digitos = new int[11];
        for (int i = 0; i < 9; i++) {
            digitos[i] = base.charAt(i) - '0';
        }
        for (int posicao = 9; posicao < 11; posicao++) {
            int soma = 0;
            for (int i = 0; i < posicao; i++) {
                soma += digitos[i] * (posicao + 1 - i);
            }
            int resto = soma % 11;
            digitos[posicao] = resto < 2 ? 0 : 11 - resto;
        }
        StringBuilder numero = new StringBuilder();
        for (int digito : digitos) {
            numero.append(digito);
        }
        return numero.toString();
    }

    private String placa() {
        int n = numero();
        return "TST" + (n % 10) + (char) ('A' + (n / 10) % 26) + String.format("%02d", (n / 260) % 100);
    }

    private String criarEmpresa() throws Exception {
        return criar("/empresas", "{\"razaoSocial\":\"Oficina " + numero() + " Ltda\",\"nomeFantasia\":\"Oficina Teste\","
                + "\"telefones\":[{\"ddd\":\"12\",\"numero\":\"3900-1000\"}],"
                + "\"endereco\":{\"estado\":\"SP\",\"cidade\":\"Jacarei\",\"bairro\":\"Centro\",\"rua\":\"Rua A\",\"numero\":\"10\",\"codigoPostal\":\"12300-000\"}}",
                "empresas");
    }

    private String criarUsuario(String perfis, String empresa) throws Exception {
        String vinculo = empresa == null ? "" : ",\"empresaId\":" + idDe(empresa);
        return criar("/usuarios", "{\"nome\":\"Usuario " + numero() + "\",\"perfis\":[" + perfis + "]" + vinculo + "}", "usuarios");
    }

    private String criarVeiculo(String proprietario) throws Exception {
        return criar("/veiculos", "{\"tipo\":\"HATCH\",\"modelo\":\"Fiat Argo\",\"placa\":\"" + placa() + "\",\"proprietarioId\":"
                + idDe(proprietario) + "}", "veiculos");
    }

    private String criarMercadoria(String empresa) throws Exception {
        return criar("/mercadorias", "{\"nome\":\"Filtro de oleo\",\"validade\":\"2035-01-01\",\"fabricao\":\"2026-01-10\","
                + "\"quantidade\":10,\"valor\":45.5,\"empresaId\":" + idDe(empresa) + "}", "mercadorias");
    }

    private String criarServico(String empresa) throws Exception {
        return criar("/servicos", "{\"nome\":\"Troca de oleo\",\"valor\":80.0,\"empresaId\":" + idDe(empresa) + "}", "servicos");
    }

    private String corpoVenda(String identificacao, Cenario c, String cliente, String funcionario, String veiculo) {
        return "{\"identificacao\":\"" + identificacao + "\",\"empresaId\":" + idDe(c.empresa)
                + ",\"clienteId\":" + idDe(cliente) + ",\"funcionarioId\":" + idDe(funcionario)
                + ",\"veiculoId\":" + idDe(veiculo) + ",\"mercadorias\":[" + idDe(c.mercadoria) + "],\"servicos\":["
                + idDe(c.servico) + "]}";
    }

    private Cenario cenario() throws Exception {
        Cenario c = new Cenario();
        c.empresa = criarEmpresa();
        c.funcionario = criarUsuario("\"FUNCIONARIO\"", c.empresa);
        c.cliente = criarUsuario("\"CLIENTE\"", c.empresa);
        c.veiculo = criarVeiculo(c.cliente);
        c.mercadoria = criarMercadoria(c.empresa);
        c.servico = criarServico(c.empresa);
        c.venda = criar("/vendas", corpoVenda("VND-T-" + numero(), c, c.cliente, c.funcionario, c.veiculo), "vendas");
        return c;
    }

    private void verificarCrud(String rota, String colecao, String location, String atualizacao, String campo, Object valor) throws Exception {
        mvc.perform(get(rota).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded." + colecao).isArray())
                .andExpect(jsonPath("$._embedded." + colecao + "[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith(rota)));

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", endsWith(location)))
                .andExpect(jsonPath("$._links." + colecao + ".href", endsWith(rota)))
                .andExpect(jsonPath("$._links.atualizar.href").exists())
                .andExpect(jsonPath("$._links.excluir.href").exists());

        enviarPut(location, atualizacao)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + campo).value(valor))
                .andExpect(jsonPath("$._links.self.href").exists());

        mvc.perform(delete(location))
                .andExpect(status().isNoContent());

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").exists());
    }

    @Test
    void crudEmpresa() throws Exception {
        String location = criarEmpresa();
        String id = idDe(location);

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.cadastro").exists())
                .andExpect(jsonPath("$.telefones[0]._links.self.href").exists())
                .andExpect(jsonPath("$.endereco._links.self.href").exists())
                .andExpect(jsonPath("$._links.usuarios.href", endsWith("/empresas/" + id + "/usuarios")))
                .andExpect(jsonPath("$._links['novo-usuario'].href", endsWith("/empresas/" + id + "/usuarios")))
                .andExpect(jsonPath("$._links.mercadorias.href", endsWith("/empresas/" + id + "/mercadorias")))
                .andExpect(jsonPath("$._links.servicos.href", endsWith("/empresas/" + id + "/servicos")))
                .andExpect(jsonPath("$._links.vendas.href", endsWith("/empresas/" + id + "/vendas")))
                .andExpect(jsonPath("$.usuarios").doesNotExist())
                .andExpect(jsonPath("$.vendas").doesNotExist());

        verificarCrud("/empresas", "empresas", location, "{\"nomeFantasia\":\"Oficina Renomeada\"}", "nomeFantasia", "Oficina Renomeada");
    }

    @Test
    void crudUsuario() throws Exception {
        String location = criar("/usuarios", "{\"nome\":\"Ana Souza\",\"nomeSocial\":\"Ana\",\"perfis\":[\"CLIENTE\",\"FORNECEDOR\"],"
                + "\"documentos\":[{\"tipo\":\"CPF\",\"numero\":\"" + cpf() + "\",\"dataEmissao\":\"2015-05-10\"}],"
                + "\"telefones\":[{\"ddd\":\"12\",\"numero\":\"99999-0000\"}],"
                + "\"emails\":[{\"endereco\":\"ana" + numero() + "@teste.com\"}],"
                + "\"credenciais\":[{\"tipo\":\"usuarioSenha\",\"nomeUsuario\":\"ana" + numero() + "\",\"senha\":\"segredo123\"}],"
                + "\"endereco\":{\"estado\":\"SP\",\"cidade\":\"Jacarei\",\"bairro\":\"Centro\",\"rua\":\"Rua A\",\"numero\":\"10\",\"codigoPostal\":\"12300000\"}}",
                "usuarios");
        String id = idDe(location);

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.perfis", hasSize(2)))
                .andExpect(jsonPath("$.perfis", hasItem("FORNECEDOR")))
                .andExpect(jsonPath("$.documentos[0].tipo").value("CPF"))
                .andExpect(jsonPath("$.documentos[0]._links.self.href").exists())
                .andExpect(jsonPath("$.telefones[0]._links.self.href").exists())
                .andExpect(jsonPath("$.emails[0]._links.self.href").exists())
                .andExpect(jsonPath("$.endereco._links.self.href").exists())
                .andExpect(jsonPath("$.credenciais[0].tipo").value("usuarioSenha"))
                .andExpect(jsonPath("$.credenciais[0]._links.usuario.href", endsWith("/usuarios/" + id)))
                .andExpect(jsonPath("$._links.veiculos.href", endsWith("/usuarios/" + id + "/veiculos")))
                .andExpect(jsonPath("$._links.vendas.href", endsWith("/usuarios/" + id + "/vendas")))
                .andExpect(jsonPath("$._links.credenciais.href", endsWith("/usuarios/" + id + "/credenciais")))
                .andExpect(content().string(not(containsString("senha"))));

        mvc.perform(get(location + "/credenciais").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.credenciais", hasSize(1)))
                .andExpect(jsonPath("$._links.usuario.href", endsWith(location)));

        verificarCrud("/usuarios", "usuarios", location, "{\"nome\":\"Ana Souza Lima\"}", "nome", "Ana Souza Lima");
    }

    @Test
    void crudTelefone() throws Exception {
        String usuario = criarUsuario("\"CLIENTE\"", null);
        String location = criar("/telefones", "{\"ddd\":\"11\",\"numero\":\"3333-4444\",\"usuarioId\":" + idDe(usuario) + "}", "telefones");

        mvc.perform(get(usuario).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.telefones[0]._links.self.href", endsWith(location)));

        String empresa = criarEmpresa();
        criar("/telefones", "{\"ddd\":\"12\",\"numero\":\"3900-2000\",\"empresaId\":" + idDe(empresa) + "}", "telefones");
        mvc.perform(get(empresa).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.telefones", hasSize(2)));

        verificarCrud("/telefones", "telefones", location, "{\"numero\":\"3333-5555\"}", "numero", "3333-5555");
    }

    @Test
    void crudEndereco() throws Exception {
        String usuario = criarUsuario("\"CLIENTE\"", null);
        String corpo = "{\"estado\":\"SP\",\"cidade\":\"Sao Jose dos Campos\",\"bairro\":\"Vila Ema\",\"rua\":\"Rua B\",\"numero\":\"20\","
                + "\"codigoPostal\":\"12243-000\",\"usuarioId\":" + idDe(usuario) + "}";
        String location = criar("/enderecos", corpo, "enderecos");

        enviarPost("/enderecos", corpo)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem", containsString("já possui endereço")));

        mvc.perform(get(usuario).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.endereco._links.self.href", endsWith(location)));

        verificarCrud("/enderecos", "enderecos", location, "{\"cidade\":\"Jacarei\"}", "cidade", "Jacarei");

        mvc.perform(get(usuario).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.endereco").doesNotExist());
    }

    @Test
    void crudDocumento() throws Exception {
        String usuario = criarUsuario("\"CLIENTE\"", null);
        String cpf = cpf();
        String location = criar("/documentos", "{\"tipo\":\"CPF\",\"numero\":\"" + cpf + "\",\"dataEmissao\":\"2020-01-15\",\"usuarioId\":"
                + idDe(usuario) + "}", "documentos");

        enviarPost("/documentos", "{\"tipo\":\"CPF\",\"numero\":\"" + cpf + "\",\"dataEmissao\":\"2020-01-15\",\"usuarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isConflict());

        enviarPut(location, "{\"tipo\":\"PASSAPORTE\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem", containsString("PASSAPORTE")));

        verificarCrud("/documentos", "documentos", location, "{\"tipo\":\"PASSAPORTE\",\"numero\":\"FX" + (100000 + numero()) + "\"}",
                "tipo", "PASSAPORTE");
    }

    @Test
    void crudEmail() throws Exception {
        String usuario = criarUsuario("\"CLIENTE\"", null);
        String location = criar("/emails", "{\"endereco\":\"contato" + numero() + "@teste.com\",\"usuarioId\":" + idDe(usuario) + "}", "emails");

        verificarCrud("/emails", "emails", location, "{\"endereco\":\"novo@teste.com\"}", "endereco", "novo@teste.com");
    }

    @Test
    void crudCredencialUsuarioSenha() throws Exception {
        String usuario = criarUsuario("\"FUNCIONARIO\"", null);
        String location = criar("/credenciais", "{\"tipo\":\"usuarioSenha\",\"nomeUsuario\":\"func" + numero() + "\",\"senha\":\"senha123\",\"usuarioId\":"
                + idDe(usuario) + "}", "credenciais");

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.tipo").value("usuarioSenha"))
                .andExpect(jsonPath("$.criacao").exists())
                .andExpect(jsonPath("$.inativo").value(false))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$._links.usuario.href", endsWith(usuario)));

        enviarPut(location, "{\"tipo\":\"codigoBarra\",\"codigo\":\"12345678\"}")
                .andExpect(status().isBadRequest());

        verificarCrud("/credenciais", "credenciais", location, "{\"tipo\":\"usuarioSenha\",\"inativo\":true}", "inativo", true);
    }

    @Test
    void crudCredencialCodigoBarra() throws Exception {
        String usuario = criarUsuario("\"CLIENTE\"", null);
        String location = criar("/credenciais", "{\"tipo\":\"codigoBarra\",\"codigo\":\"78900000" + numero() + "\",\"usuarioId\":"
                + idDe(usuario) + "}", "credenciais");

        mvc.perform(get("/credenciais").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.credenciais[?(@.tipo == 'codigoBarra')]").exists())
                .andExpect(jsonPath("$._embedded.credenciais[?(@.tipo == 'usuarioSenha')]").exists())
                .andExpect(content().string(not(containsString("senha"))));

        verificarCrud("/credenciais", "credenciais", location, "{\"tipo\":\"codigoBarra\",\"codigo\":\"1234567890123\"}",
                "codigo", "1234567890123");
    }

    @Test
    void crudMercadoria() throws Exception {
        String empresa = criarEmpresa();
        String fornecedor = criarUsuario("\"FORNECEDOR\"", null);
        String location = criar("/mercadorias", "{\"nome\":\"Vela de ignicao\",\"validade\":\"2034-06-30\",\"fabricao\":\"2025-06-30\","
                + "\"quantidade\":25,\"valor\":32.9,\"empresaId\":" + idDe(empresa) + ",\"fornecedorId\":" + idDe(fornecedor) + "}", "mercadorias");

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.fabricao").value("2025-06-30"))
                .andExpect(jsonPath("$._links.empresa.href", endsWith(empresa)))
                .andExpect(jsonPath("$._links.fornecedor.href", endsWith(fornecedor)));
        mvc.perform(get(empresa + "/mercadorias").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.mercadorias", hasSize(1)));
        mvc.perform(get(fornecedor).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.mercadorias[0]._links.self.href", endsWith(location)));

        String cliente = criarUsuario("\"CLIENTE\"", null);
        enviarPost("/mercadorias", "{\"nome\":\"Correia\",\"validade\":\"2034-06-30\",\"fabricao\":\"2025-06-30\",\"quantidade\":1,\"valor\":10,"
                + "\"empresaId\":" + idDe(empresa) + ",\"fornecedorId\":" + idDe(cliente) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem", containsString("FORNECEDOR")));

        verificarCrud("/mercadorias", "mercadorias", location, "{\"quantidade\":30}", "quantidade", 30);
    }

    @Test
    void crudServico() throws Exception {
        String empresa = criarEmpresa();
        String location = criarServico(empresa);

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.empresa.href", endsWith(empresa)));
        mvc.perform(get(empresa + "/servicos").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.servicos", hasSize(1)));

        verificarCrud("/servicos", "servicos", location, "{\"valor\":95.0}", "valor", 95.0);
    }

    @Test
    void crudVeiculo() throws Exception {
        String proprietario = criarUsuario("\"CLIENTE\"", null);
        String location = criarVeiculo(proprietario);

        mvc.perform(get(location).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.proprietario.href", endsWith(proprietario)))
                .andExpect(jsonPath("$._links.vendas.href", endsWith(location + "/vendas")))
                .andExpect(jsonPath("$.proprietario").doesNotExist());
        mvc.perform(get(proprietario + "/veiculos").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.veiculos", hasSize(1)));

        verificarCrud("/veiculos", "veiculos", location, "{\"modelo\":\"Fiat Pulse\",\"tipo\":\"SUV\"}", "tipo", "SUV");
    }

    @Test
    void crudVenda() throws Exception {
        Cenario c = cenario();

        String identificacao = "VND-T-EDIT-" + numero();
        verificarCrud("/vendas", "vendas", c.venda, "{\"identificacao\":\"" + identificacao + "\"}", "identificacao", identificacao);

        mvc.perform(get(c.cliente + "/vendas").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void fluxoCompleto() throws Exception {
        String empresa = criarEmpresa();
        String funcionario = criar(empresa + "/usuarios", "{\"nome\":\"Bruno Lima\",\"perfis\":[\"FUNCIONARIO\"]}", "usuarios");
        String cliente = criar("/usuarios", "{\"nome\":\"Joana Prado\",\"perfis\":[\"CLIENTE\",\"FORNECEDOR\"]}", "usuarios");

        enviarPut(empresa + "/usuarios/" + idDe(cliente), "")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.empresa.href", endsWith(empresa)));

        mvc.perform(get(empresa + "/usuarios").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarios", hasSize(2)))
                .andExpect(jsonPath("$._links.empresa.href", endsWith(empresa)));

        Cenario c = new Cenario();
        c.empresa = empresa;
        c.veiculo = criarVeiculo(cliente);
        c.mercadoria = criarMercadoria(empresa);
        c.servico = criarServico(empresa);
        String venda = criar("/vendas", corpoVenda("VND-F-" + numero(), c, cliente, funcionario, c.veiculo), "vendas");

        String clienteHref = mvc.perform(get(venda).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cadastro").exists())
                .andExpect(jsonPath("$.mercadorias[0]._links.self.href", endsWith(c.mercadoria)))
                .andExpect(jsonPath("$.servicos[0]._links.self.href", endsWith(c.servico)))
                .andExpect(jsonPath("$._links.cliente.href", endsWith(cliente)))
                .andExpect(jsonPath("$._links.funcionario.href", endsWith(funcionario)))
                .andExpect(jsonPath("$._links.veiculo.href", endsWith(c.veiculo)))
                .andExpect(jsonPath("$._links.empresa.href", endsWith(empresa)))
                .andReturn().getResponse().getContentAsString().replaceAll(".*\"cliente\":\\{\"href\":\"([^\"]+)\".*", "$1");

        mvc.perform(get(URI.create(clienteHref).getPath()).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.perfis", hasItem("CLIENTE")));

        mvc.perform(get(cliente + "/vendas").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.vendas", hasSize(1)));
        mvc.perform(get(funcionario + "/vendas").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.vendas", hasSize(1)));
        mvc.perform(get(c.veiculo + "/vendas").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.vendas[0]._links.self.href", endsWith(venda)));
        mvc.perform(get(empresa + "/vendas").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.vendas", hasSize(1)));

        mvc.perform(delete(empresa + "/usuarios/" + idDe(cliente)))
                .andExpect(status().isNoContent());
        mvc.perform(get(empresa + "/usuarios").accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.usuarios", hasSize(1)));
    }

    @Test
    void validacoesDaVenda() throws Exception {
        Cenario c = cenario();
        String outroCliente = criarUsuario("\"CLIENTE\"", null);
        String veiculoDeOutro = criarVeiculo(outroCliente);

        enviarPost("/vendas", corpoVenda("VND-E-" + numero(), c, c.funcionario, c.funcionario, c.veiculo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem", containsString("perfil CLIENTE")));

        enviarPost("/vendas", corpoVenda("VND-E-" + numero(), c, c.cliente, c.cliente, c.veiculo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem", containsString("perfil FUNCIONARIO")));

        enviarPost("/vendas", corpoVenda("VND-E-" + numero(), c, c.cliente, c.funcionario, veiculoDeOutro))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem", containsString("não pertence ao cliente")));

        String identificacao = mvc.perform(get(c.venda).accept(MediaTypes.HAL_JSON))
                .andReturn().getResponse().getContentAsString().replaceAll(".*\"identificacao\":\"([^\"]+)\".*", "$1");
        enviarPost("/vendas", corpoVenda(identificacao, c, c.cliente, c.funcionario, c.veiculo))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.mensagem", containsString(identificacao)));

        enviarPost("/vendas", "{\"identificacao\":\"VND-E-" + numero() + "\",\"empresaId\":" + idDe(c.empresa) + ",\"clienteId\":999999,\"funcionarioId\":"
                + idDe(c.funcionario) + ",\"servicos\":[" + idDe(c.servico) + "]}")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        enviarPost("/vendas", "{\"identificacao\":\"VND-E-" + numero() + "\",\"empresaId\":" + idDe(c.empresa) + ",\"clienteId\":" + idDe(c.cliente)
                + ",\"funcionarioId\":" + idDe(c.funcionario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem", containsString("ao menos uma mercadoria")));
    }

    @Test
    void postInvalido() throws Exception {
        String usuario = criarUsuario("\"CLIENTE\"", null);

        enviarPost("/usuarios", "{\"nomeSocial\":\"sem nome\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Dados inválidos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.campos.nome").exists())
                .andExpect(jsonPath("$.campos.perfis").exists());

        enviarPut(usuario, "{\"perfis\":[]}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.perfis").value("A lista de perfis não pode ficar vazia"));

        enviarPost("/usuarios", "{\"nome\":\"X\",\"perfis\":[\"GERENTE\"]}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        enviarPost("/veiculos", "{\"tipo\":\"SEDA\",\"modelo\":\"Onix\",\"placa\":\"12-ABCD\",\"proprietarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.placa").value("A placa deve seguir o padrão ABC-1234 ou Mercosul ABC1D23"));

        enviarPost("/documentos", "{\"tipo\":\"CPF\",\"numero\":\"12345678900\",\"dataEmissao\":\"2020-01-01\",\"usuarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.numero").value("Número inválido para o tipo CPF"));

        enviarPost("/documentos", "{\"tipo\":\"RG\",\"numero\":\"5555555\",\"dataEmissao\":\"2999-01-01\",\"usuarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.dataEmissao").exists());

        enviarPost("/telefones", "{\"ddd\":\"12\",\"numero\":\"99999-1111\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.usuarioId").value("Informe exatamente um dono: usuarioId ou empresaId"));

        enviarPost("/telefones", "{\"ddd\":\"123\",\"numero\":\"x\",\"usuarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.ddd").exists())
                .andExpect(jsonPath("$.campos.numero").exists());

        enviarPost("/emails", "{\"endereco\":\"invalido\",\"usuarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.endereco").exists());

        enviarPost("/credenciais", "{\"tipo\":\"biometria\",\"usuarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Corpo da requisição inválido ou mal formatado"));

        enviarPost("/credenciais", "{\"tipo\":\"usuarioSenha\",\"nomeUsuario\":\"abc" + numero() + "\",\"senha\":\"123\",\"usuarioId\":" + idDe(usuario) + "}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.senha").exists());

        enviarPost("/mercadorias", "{\"nome\":\"Oleo\",\"validade\":\"2020-01-01\",\"fabricao\":\"2019-01-01\",\"quantidade\":-1,\"valor\":0}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.validade").exists())
                .andExpect(jsonPath("$.campos.quantidade").exists())
                .andExpect(jsonPath("$.campos.valor").exists());

        enviarPost("/vendas", "{}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.identificacao").exists())
                .andExpect(jsonPath("$.campos.clienteId").exists())
                .andExpect(jsonPath("$.campos.funcionarioId").exists())
                .andExpect(jsonPath("$.campos.empresaId").exists());

        mvc.perform(get("/usuarios/abc").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem", containsString("'id'")));
    }

    @Test
    void exclusaoDeRegistroReferenciado() throws Exception {
        Cenario c = cenario();

        mvc.perform(delete(c.cliente))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.mensagem", containsString("venda(s)")));
        mvc.perform(delete(c.funcionario))
                .andExpect(status().isConflict());
        mvc.perform(delete(c.veiculo))
                .andExpect(status().isConflict());
        mvc.perform(delete(c.mercadoria))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem", containsString("faz parte de uma venda")));
        mvc.perform(delete(c.servico))
                .andExpect(status().isConflict());
        mvc.perform(delete(c.empresa))
                .andExpect(status().isConflict());

        mvc.perform(delete(c.venda))
                .andExpect(status().isNoContent());
        mvc.perform(delete(c.cliente))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem", containsString("veículo(s)")));
        mvc.perform(delete(c.veiculo))
                .andExpect(status().isNoContent());
        mvc.perform(delete(c.mercadoria))
                .andExpect(status().isNoContent());
        mvc.perform(delete(c.cliente))
                .andExpect(status().isNoContent());
        mvc.perform(delete(c.funcionario))
                .andExpect(status().isNoContent());
        mvc.perform(delete(c.empresa))
                .andExpect(status().isNoContent());
    }

    @Test
    void semRecursaoESemSenha() throws Exception {
        Cenario c = cenario();
        criar("/credenciais", "{\"tipo\":\"usuarioSenha\",\"nomeUsuario\":\"cli" + numero() + "\",\"senha\":\"naoAparece99\",\"usuarioId\":"
                + idDe(c.cliente) + "}", "credenciais");

        mvc.perform(get(c.venda).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.cliente").doesNotExist())
                .andExpect(jsonPath("$.funcionario").doesNotExist())
                .andExpect(jsonPath("$.veiculo").doesNotExist());
        mvc.perform(get(c.veiculo).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.proprietario").doesNotExist())
                .andExpect(jsonPath("$.vendas").doesNotExist());
        mvc.perform(get(c.cliente).accept(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.vendas").doesNotExist())
                .andExpect(jsonPath("$.veiculos").doesNotExist());

        for (String rota : new String[] { "/usuarios", "/credenciais", "/vendas", "/veiculos", "/empresas", c.cliente, c.cliente + "/credenciais" }) {
            mvc.perform(get(rota).accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(not(containsString("senha"))))
                    .andExpect(content().string(not(containsString("naoAparece99"))));
        }
    }

    @Test
    void documentacaoOpenApi() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("AutoManager API"))
                .andExpect(jsonPath("$.paths['/vendas']").exists())
                .andExpect(jsonPath("$.paths['/empresas/{id}/usuarios']").exists());
        mvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}
