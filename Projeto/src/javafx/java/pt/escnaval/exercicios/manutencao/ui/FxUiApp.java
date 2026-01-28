package pt.escnaval.exercicios.manutencao.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import pt.escnaval.exercicios.manutencao.modelo.*;
import pt.escnaval.exercicios.manutencao.servicos.ServicoManutencao;
import pt.escnaval.exercicios.manutencao.utils.DateUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FxUiApp extends Application {
    private final ServicoManutencao serv = new ServicoManutencao();
    private Stage stage;

    private Scene loginScene;
    private Scene mainScene;

    private TextField loginUser;
    private PasswordField loginPass;
    private Label loginMsg;
    private Label userLabel;
    private Label statusLabel;

    private Label dashAtivos;
    private Label dashOts;
    private Label dashMttr;
    private Label dashMtbf;
    private Label dashBacklog;
    private Label dashPlanos;
    private Label dashSla;
    private Label dashStock;
    private Label dashPedidos;
    private Label dashNotificacoes;
    private Label dashMinhasOt;
    private BarChart<String, Number> kpiChart;
    private BarChart<String, Number> backlogChart;
    private XYChart.Series<String, Number> kpiSeries;
    private XYChart.Series<String, Number> backlogSeries;
    private Timeline dashboardTimeline;
    private final Set<String> dashboardWidgets = new HashSet<>();

    private ObservableList<Ativo> ativosData;
    private FilteredList<Ativo> ativosFiltered;
    private TableView<Ativo> ativosTable;

    private ObservableList<OrdemTrabalho> otData;
    private FilteredList<OrdemTrabalho> otFiltered;
    private TableView<OrdemTrabalho> otTable;

    private ObservableList<Peca> pecaData;
    private TableView<Peca> pecaTable;

    private ObservableList<PedidoManutencao> pedidoData;
    private TableView<PedidoManutencao> pedidoTable;

    private ObservableList<Utilizador> utilizadorData;
    private TableView<Utilizador> utilizadorTable;

    private ObservableList<Fornecedor> fornecedorData;
    private TableView<Fornecedor> fornecedorTable;

    private ObservableList<Auditoria> auditoriaData;
    private FilteredList<Auditoria> auditoriaFiltered;
    private TableView<Auditoria> auditoriaTable;

    private ObservableList<Notificacao> notificacaoData;
    private TableView<Notificacao> notificacaoTable;

    private ObservableList<Parametro> parametroData;
    private TableView<Parametro> parametroTable;

    private static final class WizardTask {
        private final String descricao;
        private final int duracaoMin;

        private WizardTask(String descricao, int duracaoMin) {
            this.descricao = descricao;
            this.duracaoMin = duracaoMin;
        }
    }

    private static final class WizardConsumo {
        private final String sku;
        private final int quantidade;

        private WizardConsumo(String sku, int quantidade) {
            this.sku = sku;
            this.quantidade = quantidade;
        }
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        try {
            serv.carregarTudo();
        } catch (Exception e) {
            showError("Falha ao carregar dados: " + e.getMessage());
        }
        stage.setTitle("Gestao de Manutencao");
        loginScene = buildLoginScene();
        stage.setScene(loginScene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    private Scene buildLoginScene() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        Label title = new Label("Gestao de Manutencao");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        loginUser = new TextField();
        loginUser.setPromptText("Utilizador");
        loginPass = new PasswordField();
        loginPass.setPromptText("Senha");
        Button loginBtn = new Button("Entrar");
        loginMsg = new Label();
        loginMsg.setStyle("-fx-text-fill: #c0392b;");
        Label loginHelp = new Label("Introduza utilizador e senha.");
        loginHelp.setStyle("-fx-text-fill: #555555;");

        loginBtn.setOnAction(e -> doLogin());
        loginPass.setOnAction(e -> doLogin());

        Runnable validate = () -> {
            boolean ok = !loginUser.getText().trim().isEmpty() && !loginPass.getText().trim().isEmpty();
            loginBtn.setDisable(!ok);
            if (ok) loginMsg.setText("");
        };
        loginUser.textProperty().addListener((obs, o, n) -> validate.run());
        loginPass.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();

        root.getChildren().addAll(title, loginUser, loginPass, loginHelp, loginBtn, loginMsg);
        return new Scene(root, 500, 350);
    }

    private void doLogin() {
        String id = loginUser.getText().trim();
        String senha = loginPass.getText().trim();
        if (id.isEmpty() || senha.isEmpty()) {
            loginMsg.setText("Preencha utilizador e senha.");
            return;
        }
        Utilizador u = serv.autenticar(id, senha);
        if (u == null) {
            loginMsg.setText("Credenciais invalidas.");
            return;
        }
        userLabel = new Label("Utilizador: " + u.getNome() + " (" + u.getPerfil() + ")");
        userLabel.setStyle("-fx-font-weight: bold;");
        statusLabel = new Label();
        mainScene = buildMainScene();
        stage.setScene(mainScene);
    }

    private Scene buildMainScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox top = new HBox(12);
        top.setAlignment(Pos.CENTER_LEFT);
        Button saveBtn = new Button("Guardar");
        Button logoutBtn = new Button("Sair");
        saveBtn.setOnAction(e -> {
            try {
                serv.guardarTudo();
                statusLabel.setText("Dados guardados.");
            } catch (Exception ex) {
                showError("Erro ao guardar: " + ex.getMessage());
            }
        });
        logoutBtn.setOnAction(e -> {
            serv.setUtilizadorAtual(null);
            if (dashboardTimeline != null) {
                dashboardTimeline.stop();
            }
            loginUser.clear();
            loginPass.clear();
            loginMsg.setText("");
            stage.setScene(loginScene);
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        top.getChildren().addAll(userLabel, spacer, statusLabel, saveBtn, logoutBtn);
        root.setTop(top);

        TabPane tabs = new TabPane();
        Tab dashboardTab = buildDashboardTab();
        Tab ativosTab = buildAtivosTab();
        Tab otTab = buildOtTab();
        Tab inventarioTab = buildInventarioTab();
        Tab pedidosTab = buildPedidosTab();
        Tab utilizadoresTab = buildUtilizadoresTab();
        Tab fornecedoresTab = buildFornecedoresTab();
        Tab auditoriaTab = buildAuditoriaTab();
        Tab notificacoesTab = buildNotificacoesTab();
        Tab parametrosTab = buildParametrosTab();
        tabs.getTabs().addAll(
                dashboardTab,
                ativosTab,
                otTab,
                inventarioTab,
                pedidosTab,
                utilizadoresTab,
                fornecedoresTab,
                auditoriaTab,
                notificacoesTab,
                parametrosTab
        );
        applyRoleAccess(dashboardTab, ativosTab, otTab, inventarioTab, pedidosTab, utilizadoresTab, fornecedoresTab,
                auditoriaTab, notificacoesTab, parametrosTab);
        startDashboardAutoRefresh();
        root.setCenter(tabs);
        return new Scene(root, 1200, 800);
    }

    private Tab buildDashboardTab() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(12));

        dashAtivos = new Label();
        dashOts = new Label();
        dashMttr = new Label();
        dashMtbf = new Label();
        dashBacklog = new Label();
        dashPlanos = new Label();
        dashSla = new Label();
        dashStock = new Label();
        dashPedidos = new Label();
        dashNotificacoes = new Label();
        dashMinhasOt = new Label();
        Button refresh = new Button("Atualizar");
        refresh.setOnAction(e -> updateDashboard());
        Button config = new Button("Configurar");
        Utilizador.Perfil perfilAtual = serv.getUtilizadorAtual() == null ? Utilizador.Perfil.TECNICO : serv.getUtilizadorAtual().getPerfil();
        boolean gestorOuPlaneador = perfilAtual == Utilizador.Perfil.GESTOR || perfilAtual == Utilizador.Perfil.PLANEADOR;
        config.setDisable(!gestorOuPlaneador);
        config.setOnAction(e -> configurarDashboardDialog());

        GridPane stats = new GridPane();
        stats.setHgap(12);
        stats.setVgap(6);
        stats.addRow(0, dashAtivos, dashOts, dashPlanos, dashSla);
        stats.addRow(1, dashMttr, dashMtbf, dashBacklog, dashStock);
        stats.addRow(2, dashPedidos, dashNotificacoes, dashMinhasOt);

        CategoryAxis kpiAxis = new CategoryAxis();
        NumberAxis kpiValue = new NumberAxis();
        kpiChart = new BarChart<>(kpiAxis, kpiValue);
        kpiChart.setTitle("KPIs");
        kpiChart.setLegendVisible(false);
        kpiSeries = new XYChart.Series<>();
        kpiChart.getData().add(kpiSeries);
        kpiChart.setMaxHeight(240);

        CategoryAxis backlogAxis = new CategoryAxis();
        NumberAxis backlogValue = new NumberAxis();
        backlogChart = new BarChart<>(backlogAxis, backlogValue);
        backlogChart.setTitle("Backlog por criticidade");
        backlogChart.setLegendVisible(false);
        backlogSeries = new XYChart.Series<>();
        backlogChart.getData().add(backlogSeries);
        backlogChart.setMaxHeight(240);

        loadDashboardWidgets();
        applyDashboardVisibility(kpiChart, backlogChart);
        updateDashboard();
        box.getChildren().addAll(new HBox(8, refresh, config), stats, kpiChart, backlogChart);
        return new Tab("Dashboard", box);
    }
    private Tab buildAtivosTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));

        HBox top = new HBox(8);
        TextField filtro = new TextField();
        filtro.setPromptText("Filtrar por codigo/nome");
        Button add = new Button("Adicionar");
        Button edit = new Button("Editar");
        Button archive = new Button("Arquivar");
        Button refresh = new Button("Atualizar");
        top.getChildren().addAll(filtro, add, edit, archive, refresh);

        ativosData = FXCollections.observableArrayList(serv.listarAtivos());
        ativosFiltered = new FilteredList<>(ativosData, p -> true);

        ativosTable = new TableView<>();
        ativosTable.setEditable(true);
        TableColumn<Ativo, String> cCodigo = new TableColumn<>("Codigo");
        cCodigo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getCodigo()));
        TableColumn<Ativo, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getNome()));
        cNome.setCellFactory(TextFieldTableCell.forTableColumn());
        cNome.setOnEditCommit(e -> {
            Ativo a = e.getRowValue();
            try {
                serv.atualizarAtivo(a.getCodigo(), e.getNewValue(), null, null, null, null);
                refreshAtivos();
            } catch (Exception ex) {
                showError(ex.getMessage());
                refreshAtivos();
            }
        });
        TableColumn<Ativo, Integer> cCrit = new TableColumn<>("Crit");
        cCrit.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getCriticidade()).asObject());
        cCrit.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        cCrit.setOnEditCommit(e -> {
            Ativo a = e.getRowValue();
            try {
                serv.atualizarAtivo(a.getCodigo(), null, null, e.getNewValue(), null, null);
                refreshAtivos();
            } catch (Exception ex) {
                showError(ex.getMessage());
                refreshAtivos();
            }
        });
        TableColumn<Ativo, String> cEstado = new TableColumn<>("Estado");
        cEstado.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getEstado().name()));
        ativosTable.getColumns().addAll(cCodigo, cNome, cCrit, cEstado);

        Pagination pagination = new Pagination();
        ativosFiltered.addListener((ListChangeListener<Ativo>) c -> updatePagination(pagination, ativosFiltered));
        filtro.textProperty().addListener((obs, old, val) -> {
            String t = val == null ? "" : val.toLowerCase(Locale.ROOT);
            ativosFiltered.setPredicate(a -> t.isBlank()
                    || a.getCodigo().toLowerCase(Locale.ROOT).contains(t)
                    || a.getNome().toLowerCase(Locale.ROOT).contains(t));
            updatePagination(pagination, ativosFiltered);
            updatePage(ativosTable, ativosFiltered, pagination.getCurrentPageIndex());
        });

        pagination.currentPageIndexProperty().addListener((obs, o, n) -> updatePage(ativosTable, ativosFiltered, n.intValue()));
        updatePagination(pagination, ativosFiltered);
        updatePage(ativosTable, ativosFiltered, 0);

        add.setOnAction(e -> addAtivoDialog());
        edit.setOnAction(e -> editAtivoDialog());
        archive.setOnAction(e -> archiveAtivo());
        refresh.setOnAction(e -> refreshAtivos());

        VBox.setVgrow(ativosTable, Priority.ALWAYS);
        root.getChildren().addAll(top, ativosTable, pagination);
        return new Tab("Ativos", root);
    }

    private Tab buildOtTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));

        HBox top = new HBox(8);
        TextField filtro = new TextField();
        filtro.setPromptText("Filtrar por id/descricao");
        Button create = new Button("Criar OT");
        Button wizard = new Button("Wizard OT");
        Button state = new Button("Alterar estado");
        Button refresh = new Button("Atualizar");
        top.getChildren().addAll(filtro, create, wizard, state, refresh);

        Button atribuir = new Button("Atribuir tecnico");
        Button tarefa = new Button("Tarefa");
        Button consumo = new Button("Consumo");
        Button execucao = new Button("Execucao");
        Button documento = new Button("Documento");
        HBox extra = new HBox(8, atribuir, tarefa, consumo, execucao, documento);

        otData = FXCollections.observableArrayList(serv.listarOrdemTrabalho());
        otFiltered = new FilteredList<>(otData, p -> true);
        otTable = new TableView<>();
        otTable.setEditable(true);
        TableColumn<OrdemTrabalho, String> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getId()));
        TableColumn<OrdemTrabalho, String> cTipo = new TableColumn<>("Tipo");
        cTipo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTipo().name()));
        TableColumn<OrdemTrabalho, OrdemTrabalho.Estado> cEstado = new TableColumn<>("Estado");
        cEstado.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getEstado()));
        cEstado.setCellFactory(ComboBoxTableCell.forTableColumn(OrdemTrabalho.Estado.values()));
        cEstado.setOnEditCommit(e -> {
            OrdemTrabalho ot = e.getRowValue();
            try {
                serv.alterarEstadoOT(ot.getId(), e.getNewValue(), serv.getUtilizadorAtual().getId());
                refreshOt();
            } catch (Exception ex) {
                showError(ex.getMessage());
                refreshOt();
            }
        });
        TableColumn<OrdemTrabalho, Integer> cPrio = new TableColumn<>("Prio");
        cPrio.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getPrioridade()).asObject());
        cPrio.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        cPrio.setOnEditCommit(e -> {
            OrdemTrabalho ot = e.getRowValue();
            try {
                serv.atualizarPrioridadeOT(ot.getId(), e.getNewValue());
                refreshOt();
            } catch (Exception ex) {
                showError(ex.getMessage());
                refreshOt();
            }
        });
        TableColumn<OrdemTrabalho, String> cAtivo = new TableColumn<>("Ativo");
        cAtivo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getIdAtivo()));
        TableColumn<OrdemTrabalho, String> cDesc = new TableColumn<>("Descricao");
        cDesc.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getDescricao()));
        otTable.getColumns().addAll(cId, cTipo, cEstado, cPrio, cAtivo, cDesc);

        Pagination pagination = new Pagination();
        otFiltered.addListener((ListChangeListener<OrdemTrabalho>) c -> updatePagination(pagination, otFiltered));
        filtro.textProperty().addListener((obs, old, val) -> {
            String t = val == null ? "" : val.toLowerCase(Locale.ROOT);
            otFiltered.setPredicate(o -> t.isBlank()
                    || o.getId().toLowerCase(Locale.ROOT).contains(t)
                    || o.getDescricao().toLowerCase(Locale.ROOT).contains(t));
            updatePagination(pagination, otFiltered);
            updatePage(otTable, otFiltered, pagination.getCurrentPageIndex());
        });
        pagination.currentPageIndexProperty().addListener((obs, o, n) -> updatePage(otTable, otFiltered, n.intValue()));
        updatePagination(pagination, otFiltered);
        updatePage(otTable, otFiltered, 0);

        create.setOnAction(e -> createOtDialog());
        wizard.setOnAction(e -> createOtWizardDialog());
        state.setOnAction(e -> changeOtStateDialog());
        refresh.setOnAction(e -> refreshOt());
        atribuir.setOnAction(e -> atribuirTecnicoDialog());
        tarefa.setOnAction(e -> addTarefaDialog());
        consumo.setOnAction(e -> addConsumoDialog());
        execucao.setOnAction(e -> registarExecucaoDialog());
        documento.setOnAction(e -> anexarDocumentoDialog());

        VBox.setVgrow(otTable, Priority.ALWAYS);
        root.getChildren().addAll(top, extra, otTable, pagination);
        return new Tab("OTs", root);
    }

    private Tab buildInventarioTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        HBox top = new HBox(8);
        Button add = new Button("Adicionar peca");
        Button mov = new Button("Movimento stock");
        Button inv = new Button("Inventario ciclico");
        Button refresh = new Button("Atualizar");
        top.getChildren().addAll(add, mov, inv, refresh);

        pecaData = FXCollections.observableArrayList(serv.listarPecas());
        pecaTable = new TableView<>();
        TableColumn<Peca, String> cSku = new TableColumn<>("SKU");
        cSku.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getSku()));
        TableColumn<Peca, String> cDes = new TableColumn<>("Designacao");
        cDes.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getDesignacao()));
        TableColumn<Peca, String> cUni = new TableColumn<>("Unidade");
        cUni.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getUnidade()));
        TableColumn<Peca, String> cRep = new TableColumn<>("Reposicao");
        cRep.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(Integer.toString(cd.getValue().getPontoReposicao())));
        pecaTable.getColumns().addAll(cSku, cDes, cUni, cRep);
        pecaTable.setItems(pecaData);

        add.setOnAction(e -> addPecaDialog());
        mov.setOnAction(e -> moveStockDialog());
        inv.setOnAction(e -> inventarioDialog());
        refresh.setOnAction(e -> refreshPecas());

        VBox.setVgrow(pecaTable, Priority.ALWAYS);
        root.getChildren().addAll(top, pecaTable);
        return new Tab("Inventario", root);
    }
    private Tab buildPedidosTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        HBox top = new HBox(8);
        Button sub = new Button("Submeter");
        Button apr = new Button("Aprovar");
        Button rej = new Button("Rejeitar");
        Button conv = new Button("Converter em OT");
        Button refresh = new Button("Atualizar");
        top.getChildren().addAll(sub, apr, rej, conv, refresh);

        Utilizador.Perfil perfil = serv.getUtilizadorAtual() == null ? Utilizador.Perfil.TECNICO : serv.getUtilizadorAtual().getPerfil();
        boolean gestorOuPlaneador = perfil == Utilizador.Perfil.GESTOR || perfil == Utilizador.Perfil.PLANEADOR;
        apr.setDisable(!gestorOuPlaneador);
        rej.setDisable(!gestorOuPlaneador);
        conv.setDisable(!gestorOuPlaneador);

        pedidoData = FXCollections.observableArrayList(serv.listarPedidos());
        pedidoTable = new TableView<>();
        TableColumn<PedidoManutencao, String> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getId()));
        TableColumn<PedidoManutencao, String> cAtivo = new TableColumn<>("Ativo");
        cAtivo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getAtivoCodigo()));
        TableColumn<PedidoManutencao, String> cEstado = new TableColumn<>("Estado");
        cEstado.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getEstado().name()));
        TableColumn<PedidoManutencao, String> cDesc = new TableColumn<>("Descricao");
        cDesc.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getDescricao()));
        pedidoTable.getColumns().addAll(cId, cAtivo, cEstado, cDesc);
        pedidoTable.setItems(pedidoData);

        sub.setOnAction(e -> submitPedidoDialog());
        apr.setOnAction(e -> aprovarPedido());
        rej.setOnAction(e -> rejeitarPedido());
        conv.setOnAction(e -> converterPedido());
        refresh.setOnAction(e -> refreshPedidos());

        VBox.setVgrow(pedidoTable, Priority.ALWAYS);
        root.getChildren().addAll(top, pedidoTable);
        return new Tab("Pedidos", root);
    }

    private Tab buildUtilizadoresTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        HBox top = new HBox(8);
        Button add = new Button("Adicionar");
        Button inativar = new Button("Inativar");
        Button refresh = new Button("Atualizar");
        top.getChildren().addAll(add, inativar, refresh);

        Utilizador.Perfil perfil = serv.getUtilizadorAtual() == null ? Utilizador.Perfil.TECNICO : serv.getUtilizadorAtual().getPerfil();
        boolean gestor = perfil == Utilizador.Perfil.GESTOR;
        add.setDisable(!gestor);
        inativar.setDisable(!gestor);

        utilizadorData = FXCollections.observableArrayList(serv.listarUtilizadores());
        utilizadorTable = new TableView<>();
        TableColumn<Utilizador, String> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getId()));
        TableColumn<Utilizador, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getNome()));
        TableColumn<Utilizador, String> cPerfil = new TableColumn<>("Perfil");
        cPerfil.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getPerfil().name()));
        TableColumn<Utilizador, String> cEstado = new TableColumn<>("Estado");
        cEstado.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getEstado().name()));
        utilizadorTable.getColumns().addAll(cId, cNome, cPerfil, cEstado);
        utilizadorTable.setItems(utilizadorData);

        add.setOnAction(e -> addUtilizadorDialog());
        inativar.setOnAction(e -> inativarUtilizador());
        refresh.setOnAction(e -> refreshUtilizadores());

        VBox.setVgrow(utilizadorTable, Priority.ALWAYS);
        root.getChildren().addAll(top, utilizadorTable);
        return new Tab("Utilizadores", root);
    }

    private Tab buildFornecedoresTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        HBox top = new HBox(8);
        Button add = new Button("Adicionar");
        Button refresh = new Button("Atualizar");
        top.getChildren().addAll(add, refresh);

        Utilizador.Perfil perfil = serv.getUtilizadorAtual() == null ? Utilizador.Perfil.TECNICO : serv.getUtilizadorAtual().getPerfil();
        boolean gestorOuPlaneador = perfil == Utilizador.Perfil.GESTOR || perfil == Utilizador.Perfil.PLANEADOR;
        add.setDisable(!gestorOuPlaneador);

        fornecedorData = FXCollections.observableArrayList(serv.listarFornecedores());
        fornecedorTable = new TableView<>();
        TableColumn<Fornecedor, String> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getId()));
        TableColumn<Fornecedor, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getNome()));
        TableColumn<Fornecedor, String> cSla = new TableColumn<>("SLA");
        cSla.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(
                cd.getValue().getSlaHorasPadrao() == null ? "" : cd.getValue().getSlaHorasPadrao().toString()));
        fornecedorTable.getColumns().addAll(cId, cNome, cSla);
        fornecedorTable.setItems(fornecedorData);

        add.setOnAction(e -> addFornecedorDialog());
        refresh.setOnAction(e -> refreshFornecedores());

        VBox.setVgrow(fornecedorTable, Priority.ALWAYS);
        root.getChildren().addAll(top, fornecedorTable);
        return new Tab("Fornecedores", root);
    }

    private Tab buildAuditoriaTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        TextField filtro = new TextField();
        filtro.setPromptText("Filtro entidade/id");

        auditoriaData = FXCollections.observableArrayList(serv.listarAuditoria());
        auditoriaFiltered = new FilteredList<>(auditoriaData, p -> true);
        auditoriaTable = new TableView<>();
        TableColumn<Auditoria, String> cTs = new TableColumn<>("Data");
        cTs.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTimestamp().toString()));
        TableColumn<Auditoria, String> cEnt = new TableColumn<>("Entidade");
        cEnt.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getEntidade()));
        TableColumn<Auditoria, String> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getIdEntidade()));
        TableColumn<Auditoria, String> cCampo = new TableColumn<>("Campo");
        cCampo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getCampo()));
        TableColumn<Auditoria, String> cNovo = new TableColumn<>("Novo");
        cNovo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getValorNovo()));
        auditoriaTable.getColumns().addAll(cTs, cEnt, cId, cCampo, cNovo);
        auditoriaTable.setItems(auditoriaFiltered);

        filtro.textProperty().addListener((obs, old, val) -> {
            String t = val == null ? "" : val.toLowerCase(Locale.ROOT);
            auditoriaFiltered.setPredicate(a -> t.isBlank()
                    || a.getEntidade().toLowerCase(Locale.ROOT).contains(t)
                    || a.getIdEntidade().toLowerCase(Locale.ROOT).contains(t));
        });

        VBox.setVgrow(auditoriaTable, Priority.ALWAYS);
        root.getChildren().addAll(filtro, auditoriaTable);
        return new Tab("Auditoria", root);
    }

    private Tab buildNotificacoesTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        Button gerar = new Button("Gerar");
        Button refresh = new Button("Atualizar");

        notificacaoData = FXCollections.observableArrayList(serv.listarNotificacoes());
        notificacaoTable = new TableView<>();
        TableColumn<Notificacao, String> cTipo = new TableColumn<>("Tipo");
        cTipo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTipo()));
        TableColumn<Notificacao, String> cMsg = new TableColumn<>("Mensagem");
        cMsg.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getMensagem()));
        TableColumn<Notificacao, String> cEstado = new TableColumn<>("Estado");
        cEstado.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getEstado().name()));
        notificacaoTable.getColumns().addAll(cTipo, cMsg, cEstado);
        notificacaoTable.setItems(notificacaoData);

        gerar.setOnAction(e -> {
            serv.gerarNotificacoes();
            serv.marcarNotificacoesEnviadas();
            refreshNotificacoes();
        });
        refresh.setOnAction(e -> refreshNotificacoes());

        VBox.setVgrow(notificacaoTable, Priority.ALWAYS);
        root.getChildren().addAll(new HBox(8, gerar, refresh), notificacaoTable);
        return new Tab("Notificacoes", root);
    }

    private Tab buildParametrosTab() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        parametroData = FXCollections.observableArrayList(serv.listarParametros());
        parametroTable = new TableView<>();
        Utilizador.Perfil perfil = serv.getUtilizadorAtual() == null ? Utilizador.Perfil.TECNICO : serv.getUtilizadorAtual().getPerfil();
        boolean gestorOuPlaneador = perfil == Utilizador.Perfil.GESTOR || perfil == Utilizador.Perfil.PLANEADOR;
        parametroTable.setEditable(gestorOuPlaneador);

        TableColumn<Parametro, String> cTipo = new TableColumn<>("Tipo");
        cTipo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTipo().name()));
        TableColumn<Parametro, String> cCodigo = new TableColumn<>("Codigo");
        cCodigo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getCodigo()));
        cCodigo.setCellFactory(TextFieldTableCell.forTableColumn());
        cCodigo.setOnEditCommit(e -> {
            Parametro p = e.getRowValue();
            try {
                serv.atualizarParametro(p.getId(), e.getNewValue(), null);
                refreshParametros();
            } catch (Exception ex) {
                showError(ex.getMessage());
                refreshParametros();
            }
        });
        TableColumn<Parametro, String> cDesc = new TableColumn<>("Descricao");
        cDesc.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getDescricao()));
        cDesc.setCellFactory(TextFieldTableCell.forTableColumn());
        cDesc.setOnEditCommit(e -> {
            Parametro p = e.getRowValue();
            try {
                serv.atualizarParametro(p.getId(), null, e.getNewValue());
                refreshParametros();
            } catch (Exception ex) {
                showError(ex.getMessage());
                refreshParametros();
            }
        });
        parametroTable.getColumns().addAll(cTipo, cCodigo, cDesc);
        parametroTable.setItems(parametroData);
        VBox.setVgrow(parametroTable, Priority.ALWAYS);
        root.getChildren().addAll(parametroTable);
        return new Tab("Parametros", root);
    }
    private void refreshAtivos() {
        ativosData.setAll(serv.listarAtivos());
        updatePaginationForTable(ativosTable, ativosFiltered);
    }

    private void refreshOt() {
        otData.setAll(serv.listarOrdemTrabalho());
        updatePaginationForTable(otTable, otFiltered);
    }

    private void refreshPecas() {
        pecaData.setAll(serv.listarPecas());
    }

    private void refreshPedidos() {
        pedidoData.setAll(serv.listarPedidos());
    }

    private void refreshUtilizadores() {
        utilizadorData.setAll(serv.listarUtilizadores());
    }

    private void refreshFornecedores() {
        fornecedorData.setAll(serv.listarFornecedores());
    }

    private void refreshNotificacoes() {
        notificacaoData.setAll(serv.listarNotificacoes());
    }

    private void refreshParametros() {
        parametroData.setAll(serv.listarParametros());
        parametroTable.refresh();
    }

    private void loadDashboardWidgets() {
        dashboardWidgets.clear();
        Utilizador u = serv.getUtilizadorAtual();
        DashboardConfig cfg = u == null ? null : serv.obterDashboardPorPerfil(u.getPerfil());
        if (cfg == null || cfg.getWidgets() == null || cfg.getWidgets().isBlank()) {
            dashboardWidgets.add("ATIVOS");
            dashboardWidgets.add("BACKLOG");
            dashboardWidgets.add("MTTR");
            dashboardWidgets.add("MTBF");
            dashboardWidgets.add("STOCK");
            dashboardWidgets.add("PLANOS");
            dashboardWidgets.add("SLA");
            dashboardWidgets.add("PEDIDOS");
            dashboardWidgets.add("NOTIFICACOES");
            dashboardWidgets.add("MINHAS_OT");
            return;
        }
        for (String w : cfg.getWidgets().split(",")) {
            String widget = w.trim().toUpperCase(Locale.ROOT);
            if (!widget.isBlank()) dashboardWidgets.add(widget);
        }
    }

    private boolean showWidget(String widget) {
        if (dashboardWidgets.isEmpty()) return true;
        return dashboardWidgets.contains(widget);
    }

    private void setWidgetVisible(Control control, boolean visible) {
        if (control == null) return;
        control.setVisible(visible);
        control.setManaged(visible);
    }

    private void applyDashboardVisibility(BarChart<String, Number> kpiChart, BarChart<String, Number> backlogChart) {
        setWidgetVisible(dashAtivos, showWidget("ATIVOS"));
        setWidgetVisible(dashOts, showWidget("BACKLOG"));
        setWidgetVisible(dashBacklog, showWidget("BACKLOG"));
        setWidgetVisible(dashMttr, showWidget("MTTR"));
        setWidgetVisible(dashMtbf, showWidget("MTBF"));
        setWidgetVisible(dashPlanos, showWidget("PLANOS"));
        setWidgetVisible(dashSla, showWidget("SLA"));
        setWidgetVisible(dashStock, showWidget("STOCK"));
        setWidgetVisible(dashPedidos, showWidget("PEDIDOS"));
        setWidgetVisible(dashNotificacoes, showWidget("NOTIFICACOES"));
        setWidgetVisible(dashMinhasOt, showWidget("MINHAS_OT"));

        boolean showKpi = showWidget("MTTR") || showWidget("MTBF") || showWidget("SLA") || showWidget("PLANOS");
        boolean showBacklog = showWidget("BACKLOG");
        if (kpiChart != null) {
            kpiChart.setVisible(showKpi);
            kpiChart.setManaged(showKpi);
        }
        if (backlogChart != null) {
            backlogChart.setVisible(showBacklog);
            backlogChart.setManaged(showBacklog);
        }
    }

    private void updateDashboard() {
        if (dashAtivos == null) return;
        if (showWidget("ATIVOS")) dashAtivos.setText("Ativos: " + serv.listarAtivos().size());
        if (showWidget("BACKLOG")) {
            dashOts.setText("OTs abertas: " + serv.backlogTotal());
            dashBacklog.setText("Backlog: " + serv.backlogPorCriticidade());
        }
        if (showWidget("MTTR")) dashMttr.setText(String.format(Locale.ROOT, "MTTR (min): %.2f", serv.calcularMTTRMin()));
        if (showWidget("MTBF")) dashMtbf.setText(String.format(Locale.ROOT, "MTBF (h): %.2f", serv.calcularMTBFHoras()));
        if (showWidget("PLANOS")) dashPlanos.setText(String.format(Locale.ROOT, "Cumprimento planos (%%): %.2f", serv.cumprimentoPlanosPercent()));
        if (showWidget("SLA")) dashSla.setText(String.format(Locale.ROOT, "Cumprimento SLA (%%): %.2f", serv.cumprimentoSlaPercent()));
        if (showWidget("STOCK")) dashStock.setText("Pecas: " + serv.listarPecas().size());
        if (showWidget("PEDIDOS")) dashPedidos.setText("Pedidos: " + serv.listarPedidos().size());
        if (showWidget("NOTIFICACOES")) dashNotificacoes.setText("Notificacoes: " + serv.listarNotificacoes().size());
        if (showWidget("MINHAS_OT")) {
            Utilizador u = serv.getUtilizadorAtual();
            long count = u == null ? 0 : serv.listarOrdemTrabalho().stream()
                    .filter(o -> u.getId().equalsIgnoreCase(o.getIdTecnico()))
                    .count();
            dashMinhasOt.setText("Minhas OTs: " + count);
        }

        if (kpiSeries != null) {
            kpiSeries.getData().setAll(
                    new XYChart.Data<>("MTTR", serv.calcularMTTRMin()),
                    new XYChart.Data<>("MTBF", serv.calcularMTBFHoras()),
                    new XYChart.Data<>("SLA%", serv.cumprimentoSlaPercent()),
                    new XYChart.Data<>("Planos%", serv.cumprimentoPlanosPercent())
            );
        }
        if (backlogSeries != null) {
            backlogSeries.getData().clear();
            for (var entry : serv.backlogPorCriticidade().entrySet()) {
                backlogSeries.getData().add(new XYChart.Data<>(Integer.toString(entry.getKey()), entry.getValue()));
            }
        }
    }

    private void startDashboardAutoRefresh() {
        if (dashboardTimeline != null) {
            dashboardTimeline.stop();
        }
        dashboardTimeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> updateDashboard()));
        dashboardTimeline.setCycleCount(Timeline.INDEFINITE);
        dashboardTimeline.play();
    }

    private <T> void updatePaginationForTable(TableView<T> table, FilteredList<T> filtered) {
        updatePage(table, filtered, 0);
    }

    private int getPageSize() {
        Utilizador u = serv.getUtilizadorAtual();
        if (u == null) return 10;
        DashboardConfig cfg = serv.obterDashboardPorPerfil(u.getPerfil());
        return cfg != null && cfg.getPageSize() != null ? cfg.getPageSize() : 10;
    }

    private <T> void updatePagination(Pagination pagination, FilteredList<T> filtered) {
        int pageSize = getPageSize();
        int total = filtered.size();
        int pages = Math.max(1, (int) Math.ceil((double) total / pageSize));
        pagination.setPageCount(pages);
        int current = Math.min(pagination.getCurrentPageIndex(), pages - 1);
        pagination.setCurrentPageIndex(current);
    }

    private <T> void updatePage(TableView<T> table, FilteredList<T> filtered, int pageIndex) {
        int pageSize = getPageSize();
        int from = pageIndex * pageSize;
        int to = Math.min(from + pageSize, filtered.size());
        if (from > to) from = 0;
        List<T> slice = filtered.subList(from, to);
        table.setItems(FXCollections.observableArrayList(slice));
    }

    private void addAtivoDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Adicionar ativo");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField codigo = new TextField();
        TextField nome = new TextField();
        TextField crit = new TextField();
        Label help = new Label("Criticidade 1-5. Codigo e nome obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("Codigo"), codigo);
        gp.addRow(1, new Label("Nome"), nome);
        gp.addRow(2, new Label("Criticidade (1-5)"), crit);
        gp.add(help, 0, 3, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(codigo.getText()) != null
                    && trimToNull(nome.getText()) != null
                    && isIntInRange(crit.getText(), 1, 5);
            okBtn.setDisable(!ok);
            help.setText(ok ? "Criticidade 1-5. Codigo e nome obrigatorios." : "Preencha codigo, nome e criticidade valida.");
        };
        codigo.textProperty().addListener((obs, o, n) -> validate.run());
        nome.textProperty().addListener((obs, o, n) -> validate.run());
        crit.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    int c = Integer.parseInt(crit.getText().trim());
                    serv.adicionarAtivo(codigo.getText().trim(), nome.getText().trim(), c, null, null);
                    refreshAtivos();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void editAtivoDialog() {
        Ativo a = ativosTable.getSelectionModel().getSelectedItem();
        if (a == null) return;
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Editar ativo");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField nome = new TextField(a.getNome());
        TextField crit = new TextField(Integer.toString(a.getCriticidade()));
        Label help = new Label("Nome obrigatorio. Criticidade 1-5.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("Nome"), nome);
        gp.addRow(1, new Label("Criticidade (1-5)"), crit);
        gp.add(help, 0, 2, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(nome.getText()) != null && isIntInRange(crit.getText(), 1, 5);
            okBtn.setDisable(!ok);
            help.setText(ok ? "Nome obrigatorio. Criticidade 1-5." : "Corrija nome ou criticidade.");
        };
        nome.textProperty().addListener((obs, o, n) -> validate.run());
        crit.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    Integer c = Integer.parseInt(crit.getText().trim());
                    serv.atualizarAtivo(a.getCodigo(), nome.getText().trim(), null, c, null, null);
                    refreshAtivos();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void archiveAtivo() {
        Ativo a = ativosTable.getSelectionModel().getSelectedItem();
        if (a == null) return;
        try {
            serv.arquivarAtivo(a.getCodigo());
            refreshAtivos();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void createOtDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Criar OT");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField ativo = new TextField();
        TextField desc = new TextField();
        ComboBox<OrdemTrabalho.Tipo> tipo = new ComboBox<>(FXCollections.observableArrayList(OrdemTrabalho.Tipo.values()));
        tipo.getSelectionModel().select(OrdemTrabalho.Tipo.CORRETIVA);
        TextField prio = new TextField("0");
        Label help = new Label("Ativo e descricao obrigatorios. Prioridade 0-5.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("Ativo"), ativo);
        gp.addRow(1, new Label("Descricao"), desc);
        gp.addRow(2, new Label("Tipo"), tipo);
        gp.addRow(3, new Label("Prioridade (0 auto)"), prio);
        gp.add(help, 0, 4, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(ativo.getText()) != null
                    && trimToNull(desc.getText()) != null
                    && (trimToNull(prio.getText()) == null || isIntInRange(prio.getText(), 0, 5));
            okBtn.setDisable(!ok);
            help.setText(ok ? "Ativo e descricao obrigatorios. Prioridade 0-5." : "Preencha ativo/descricao e prioridade valida.");
        };
        ativo.textProperty().addListener((obs, o, n) -> validate.run());
        desc.textProperty().addListener((obs, o, n) -> validate.run());
        prio.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    Integer prioVal = parseOptionalInt(prio.getText(), "Prioridade");
                    int p = prioVal == null ? 0 : prioVal;
                    serv.criarOrdemTrabalho(tipo.getValue(), p, desc.getText().trim(), ativo.getText().trim(), null, null, null);
                    refreshOt();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void changeOtStateDialog() {
        OrdemTrabalho ot = otTable.getSelectionModel().getSelectedItem();
        if (ot == null) return;
        ChoiceDialog<OrdemTrabalho.Estado> dlg = new ChoiceDialog<>(ot.getEstado(), OrdemTrabalho.Estado.values());
        dlg.setTitle("Estado OT");
        dlg.setHeaderText("Alterar estado da OT " + ot.getId());
        dlg.showAndWait().ifPresent(novo -> {
            try {
                serv.alterarEstadoOT(ot.getId(), novo, serv.getUtilizadorAtual().getId());
                refreshOt();
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    private void createOtWizardDialog() {
        Utilizador atual = serv.getUtilizadorAtual();
        if (atual == null) return;

        Stage wizard = new Stage();
        wizard.initOwner(stage);
        wizard.initModality(Modality.APPLICATION_MODAL);
        wizard.setTitle("Wizard OT");

        BorderPane root = new BorderPane();
        TabPane steps = new TabPane();
        steps.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Label validation = new Label();
        validation.setStyle("-fx-text-fill: #c0392b;");

        TextField ativo = new TextField();
        TextField desc = new TextField();
        ComboBox<OrdemTrabalho.Tipo> tipo = new ComboBox<>(FXCollections.observableArrayList(OrdemTrabalho.Tipo.values()));
        tipo.getSelectionModel().select(OrdemTrabalho.Tipo.CORRETIVA);
        TextField prio = new TextField("0");
        ComboBox<Parametro> categoria = new ComboBox<>(FXCollections.observableArrayList(parametrosPorTipo(Parametro.Tipo.CATEGORIA_FALHA)));
        ComboBox<Parametro> centro = new ComboBox<>(FXCollections.observableArrayList(parametrosPorTipo(Parametro.Tipo.CENTRO_CUSTO)));
        TextField limite = new TextField();
        limite.setPromptText("yyyy-MM-dd HH:mm");
        CheckBox slaManual = new CheckBox("SLA manual");
        TextField slaHoras = new TextField();
        slaHoras.setDisable(true);
        slaManual.selectedProperty().addListener((obs, old, val) -> slaHoras.setDisable(!val));
        Label detailHelp = new Label("Ativo e descricao obrigatorios. Prioridade 0-5.");
        detailHelp.setStyle("-fx-text-fill: #555555;");

        GridPane detail = new GridPane();
        detail.setHgap(8);
        detail.setVgap(8);
        detail.addRow(0, new Label("Ativo"), ativo);
        detail.addRow(1, new Label("Descricao"), desc);
        detail.addRow(2, new Label("Tipo"), tipo);
        detail.addRow(3, new Label("Prioridade (0 auto)"), prio);
        detail.addRow(4, new Label("Categoria falha"), categoria);
        detail.addRow(5, new Label("Centro custo"), centro);
        detail.addRow(6, new Label("Data limite"), limite);
        detail.addRow(7, slaManual, slaHoras);
        detail.add(detailHelp, 0, 8, 2, 1);

        List<WizardTask> wizardTasks = new ArrayList<>();
        ObservableList<String> taskItems = FXCollections.observableArrayList();
        ListView<String> taskList = new ListView<>(taskItems);
        TextField taskDesc = new TextField();
        TextField taskDur = new TextField();
        Button addTask = new Button("Adicionar tarefa");
        Button delTask = new Button("Remover tarefa");
        Label taskHelp = new Label("Tarefas e consumos sao opcionais.");
        taskHelp.setStyle("-fx-text-fill: #555555;");
        addTask.setOnAction(e -> {
            String d = trimToNull(taskDesc.getText());
            if (d == null || !isPositiveInt(taskDur.getText())) {
                validation.setText("Descricao e duracao de tarefa invalidas.");
                return;
            }
            int dur = Integer.parseInt(taskDur.getText().trim());
            wizardTasks.add(new WizardTask(d, dur));
            taskItems.add(d + " (" + dur + " min)");
            taskDesc.clear();
            taskDur.clear();
        });
        delTask.setOnAction(e -> {
            int idx = taskList.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                wizardTasks.remove(idx);
                taskItems.remove(idx);
            }
        });

        List<WizardConsumo> wizardConsumos = new ArrayList<>();
        ObservableList<String> consumoItems = FXCollections.observableArrayList();
        ListView<String> consumoList = new ListView<>(consumoItems);
        ComboBox<String> consumoSku = new ComboBox<>(FXCollections.observableArrayList(listarSkus()));
        consumoSku.setEditable(true);
        TextField consumoQtd = new TextField();
        Button addConsumo = new Button("Adicionar consumo");
        Button delConsumo = new Button("Remover consumo");
        addConsumo.setOnAction(e -> {
            String sku = trimToNull(comboText(consumoSku));
            if (sku == null || !isPositiveInt(consumoQtd.getText())) {
                validation.setText("SKU e quantidade de consumo invalidos.");
                return;
            }
            int qtd = Integer.parseInt(consumoQtd.getText().trim());
            wizardConsumos.add(new WizardConsumo(sku, qtd));
            consumoItems.add(sku + " x" + qtd);
            consumoSku.getEditor().clear();
            consumoQtd.clear();
        });
        delConsumo.setOnAction(e -> {
            int idx = consumoList.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                wizardConsumos.remove(idx);
                consumoItems.remove(idx);
            }
        });

        GridPane resources = new GridPane();
        resources.setHgap(8);
        resources.setVgap(8);
        resources.addRow(0, new Label("Tarefa"), taskDesc, new Label("Duracao (min)"), taskDur, addTask);
        resources.add(taskList, 0, 1, 4, 1);
        resources.add(delTask, 4, 1);
        resources.addRow(2, new Label("SKU"), consumoSku, new Label("Quantidade"), consumoQtd, addConsumo);
        resources.add(consumoList, 0, 3, 4, 1);
        resources.add(delConsumo, 4, 3);
        resources.add(taskHelp, 0, 4, 5, 1);

        TextField fornecedorId = new TextField();
        TextField ordemExterna = new TextField();
        TextField custoPrevisto = new TextField();
        CheckBox aprovarPedido = new CheckBox("Confirmo envio para aprovacao");
        Label approvalHelp = new Label("Se for EXTERNA, fornecedor e custo sao obrigatorios.");
        approvalHelp.setStyle("-fx-text-fill: #555555;");
        GridPane approval = new GridPane();
        approval.setHgap(8);
        approval.setVgap(8);
        approval.addRow(0, new Label("Fornecedor ID"), fornecedorId);
        approval.addRow(1, new Label("Ordem externa"), ordemExterna);
        approval.addRow(2, new Label("Custo previsto"), custoPrevisto);
        approval.addRow(3, aprovarPedido);
        approval.add(approvalHelp, 0, 4, 2, 1);

        Runnable updateExterna = () -> {
            boolean externa = tipo.getValue() == OrdemTrabalho.Tipo.EXTERNA;
            fornecedorId.setDisable(!externa);
            ordemExterna.setDisable(!externa);
            custoPrevisto.setDisable(!externa);
        };
        tipo.valueProperty().addListener((obs, old, val) -> updateExterna.run());
        updateExterna.run();

        TextField execInicio = new TextField();
        execInicio.setPromptText("yyyy-MM-dd HH:mm");
        TextField execFim = new TextField();
        execFim.setPromptText("yyyy-MM-dd HH:mm");
        TextField execCausa = new TextField();
        TextField execAcao = new TextField();
        ComboBox<String> execTecnico = new ComboBox<>(FXCollections.observableArrayList(listarTecnicosAtivos()));
        execTecnico.setEditable(true);
        TextField execObs = new TextField();
        CheckBox execNow = new CheckBox("Registar execucao agora");
        Label resumo = new Label();
        resumo.setWrapText(true);

        GridPane finish = new GridPane();
        finish.setHgap(8);
        finish.setVgap(8);
        finish.add(resumo, 0, 0, 2, 1);
        finish.addRow(1, execNow);
        finish.addRow(2, new Label("Inicio"), execInicio);
        finish.addRow(3, new Label("Fim"), execFim);
        finish.addRow(4, new Label("Causa"), execCausa);
        finish.addRow(5, new Label("Acao"), execAcao);
        finish.addRow(6, new Label("Tecnico"), execTecnico);
        finish.addRow(7, new Label("Observacoes"), execObs);

        Runnable toggleExec = () -> {
            boolean on = execNow.isSelected();
            execInicio.setDisable(!on);
            execFim.setDisable(!on);
            execCausa.setDisable(!on);
            execAcao.setDisable(!on);
            execTecnico.setDisable(!on);
            execObs.setDisable(!on);
        };
        execNow.selectedProperty().addListener((obs, o, n) -> toggleExec.run());
        toggleExec.run();

        Tab tDetail = new Tab("Detalhe", detail);
        Tab tResources = new Tab("Recursos", resources);
        Tab tApproval = new Tab("Aprovacao", approval);
        Tab tFinish = new Tab("Conclusao", finish);
        steps.getTabs().addAll(tDetail, tResources, tApproval, tFinish);

        Button back = new Button("Voltar");
        Button next = new Button("Seguinte");
        Button finishBtn = new Button("Concluir");
        Button cancel = new Button("Cancelar");

        final int totalSteps = 4;
        final int[] step = {0};

        Runnable updateResumo = () -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Ativo: ").append(ativo.getText()).append("\n");
            sb.append("Descricao: ").append(desc.getText()).append("\n");
            sb.append("Tipo: ").append(tipo.getValue()).append("\n");
            sb.append("Prioridade: ").append(prio.getText()).append("\n");
            sb.append("Tarefas: ").append(wizardTasks.size()).append(" | Consumos: ").append(wizardConsumos.size()).append("\n");
            resumo.setText(sb.toString());
        };

        Runnable validateDetail = () -> {
            boolean ok = trimToNull(ativo.getText()) != null
                    && trimToNull(desc.getText()) != null
                    && (trimToNull(prio.getText()) == null || isIntInRange(prio.getText(), 0, 5));
            detailHelp.setText(ok ? "Ativo e descricao obrigatorios. Prioridade 0-5." : "Preencha ativo/descricao e prioridade valida.");
        };

        Runnable updateNav = () -> {
            back.setDisable(step[0] == 0);
            next.setDisable(step[0] >= totalSteps - 1);
            finishBtn.setDisable(step[0] != totalSteps - 1);
            if (step[0] == totalSteps - 1) updateResumo.run();
        };

        ativo.textProperty().addListener((obs, o, n) -> validateDetail.run());
        desc.textProperty().addListener((obs, o, n) -> validateDetail.run());
        prio.textProperty().addListener((obs, o, n) -> validateDetail.run());
        validateDetail.run();
        updateNav.run();

        steps.getSelectionModel().selectedIndexProperty().addListener((obs, o, n) -> {
            step[0] = n.intValue();
            updateNav.run();
        });

        back.setOnAction(e -> {
            int idx = steps.getSelectionModel().getSelectedIndex();
            if (idx > 0) steps.getSelectionModel().select(idx - 1);
        });
        next.setOnAction(e -> {
            if (step[0] == 0) {
                if (trimToNull(ativo.getText()) == null || trimToNull(desc.getText()) == null) {
                    validation.setText("Ativo e descricao sao obrigatorios.");
                    return;
                }
                if (trimToNull(prio.getText()) != null && !isIntInRange(prio.getText(), 0, 5)) {
                    validation.setText("Prioridade invalida.");
                    return;
                }
            }
            validation.setText("");
            int idx = steps.getSelectionModel().getSelectedIndex();
            if (idx < totalSteps - 1) steps.getSelectionModel().select(idx + 1);
        });

        finishBtn.setOnAction(e -> {
            try {
                String ativoId = trimToNull(ativo.getText());
                String descricao = trimToNull(desc.getText());
                if (ativoId == null || descricao == null) {
                    throw new IllegalArgumentException("Ativo e descricao sao obrigatorios");
                }
                OrdemTrabalho.Tipo tipoVal = tipo.getValue();
                Integer prioVal = parseOptionalInt(prio.getText(), "Prioridade");
                int prioridade = prioVal == null ? 0 : prioVal;
                String categoriaVal = categoria.getValue() == null ? null : categoria.getValue().getCodigo();
                String centroVal = centro.getValue() == null ? null : centro.getValue().getCodigo();
                Instant dataLimite = parseOptionalUserDateTime(limite.getText(), "Data limite");
                Integer sla = null;
                if (slaManual.isSelected()) {
                    sla = parseOptionalInt(slaHoras.getText(), "SLA");
                    if (sla == null) throw new IllegalArgumentException("SLA obrigatorio");
                }
                String fornecedor = null;
                String ordemExt = null;
                Double custoPrev = null;
                if (tipoVal == OrdemTrabalho.Tipo.EXTERNA) {
                    fornecedor = trimToNull(fornecedorId.getText());
                    if (fornecedor == null) throw new IllegalArgumentException("Fornecedor obrigatorio");
                    ordemExt = trimToNull(ordemExterna.getText());
                    custoPrev = parseOptionalDouble(custoPrevisto.getText(), "Custo previsto");
                    if (custoPrev == null) throw new IllegalArgumentException("Custo previsto obrigatorio");
                }

                if (atual.getPerfil() == Utilizador.Perfil.SOLICITANTE || atual.getPerfil() == Utilizador.Perfil.TECNICO) {
                    if (!aprovarPedido.isSelected()) {
                        throw new IllegalArgumentException("Confirmacao de aprovacao obrigatoria.");
                    }
                    serv.submeterPedido(ativoId, descricao, atual.getId());
                    refreshPedidos();
                    showInfo("Pedido submetido para aprovacao.");
                    wizard.close();
                    return;
                }

                OrdemTrabalho ot = serv.criarOrdemTrabalhoCompleta(tipoVal, prioridade, descricao, ativoId,
                        categoriaVal, centroVal, dataLimite, sla, fornecedor, ordemExt, custoPrev);
                for (WizardTask t : wizardTasks) {
                    serv.adicionarTarefa(ot.getId(), t.descricao, t.duracaoMin);
                }
                for (WizardConsumo c : wizardConsumos) {
                    serv.adicionarConsumoPeca(ot.getId(), c.sku, c.quantidade);
                }
                if (execNow.isSelected()) {
                    Instant ini = parseOptionalUserDateTime(execInicio.getText(), "Inicio");
                    Instant fim = parseOptionalUserDateTime(execFim.getText(), "Fim");
                    String causaVal = trimToNull(execCausa.getText());
                    String acaoVal = trimToNull(execAcao.getText());
                    String tecnicoVal = trimToNull(comboText(execTecnico));
                    String obsVal = trimToNull(execObs.getText());
                    serv.registarExecucao(ot.getId(), ini, fim, causaVal, acaoVal, tecnicoVal, obsVal);
                }
                refreshOt();
                showInfo("Wizard concluido. OT: " + ot.getId());
                wizard.close();
            } catch (Exception ex) {
                validation.setText(ex.getMessage());
            }
        });
        cancel.setOnAction(e -> wizard.close());

        HBox nav = new HBox(8, back, next, finishBtn, cancel);
        nav.setAlignment(Pos.CENTER_RIGHT);

        root.setCenter(steps);
        root.setBottom(new VBox(6, validation, nav));
        BorderPane.setMargin(nav, new Insets(10, 0, 0, 0));

        Scene scene = new Scene(root, 720, 520);
        wizard.setScene(scene);
        wizard.showAndWait();
    }

    private void atribuirTecnicoDialog() {
        OrdemTrabalho sel = otTable.getSelectionModel().getSelectedItem();
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Atribuir tecnico");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField otId = new TextField(sel == null ? "" : sel.getId());
        ComboBox<String> tecnico = new ComboBox<>(FXCollections.observableArrayList(listarTecnicosAtivos()));
        tecnico.setEditable(true);
        if (sel != null && sel.getIdTecnico() != null) tecnico.setValue(sel.getIdTecnico());
        TextField inicio = new TextField();
        inicio.setPromptText("yyyy-MM-dd HH:mm");
        if (sel != null && sel.getDataInicio() != null) {
            inicio.setText(DateUtils.formatUserDateTime(sel.getDataInicio()));
        }
        Label help = new Label("OT e tecnico obrigatorios. Data deve ser valida.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("OT ID"), otId);
        gp.addRow(1, new Label("Tecnico"), tecnico);
        gp.addRow(2, new Label("Inicio planeado"), inicio);
        gp.add(help, 0, 3, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(otId.getText()) != null
                    && trimToNull(comboText(tecnico)) != null
                    && isValidDateTimeOptional(inicio.getText());
            okBtn.setDisable(!ok);
            help.setText(ok ? "OT e tecnico obrigatorios. Data deve ser valida." : "Preencha OT/tecnico e data valida.");
        };
        otId.textProperty().addListener((obs, o, n) -> validate.run());
        tecnico.valueProperty().addListener((obs, o, n) -> validate.run());
        tecnico.getEditor().textProperty().addListener((obs, o, n) -> validate.run());
        inicio.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            try {
                String id = trimToNull(otId.getText());
                String tecnicoId = trimToNull(comboText(tecnico));
                if (id == null || tecnicoId == null) throw new IllegalArgumentException("OT e tecnico obrigatorios");
                Instant ini = parseOptionalUserDateTime(inicio.getText(), "Inicio planeado");
                serv.atribuirTecnico(id, tecnicoId, ini);
                refreshOt();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void addTarefaDialog() {
        OrdemTrabalho sel = otTable.getSelectionModel().getSelectedItem();
        addTarefaDialog(sel == null ? null : sel.getId(), false);
    }

    private void addTarefaDialog(String otIdDefault, boolean lockOtId) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Adicionar tarefa");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField otId = new TextField(otIdDefault == null ? "" : otIdDefault);
        if (lockOtId) otId.setDisable(true);
        TextField desc = new TextField();
        TextField dur = new TextField();
        Label help = new Label("OT, descricao e duracao > 0 obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("OT ID"), otId);
        gp.addRow(1, new Label("Descricao"), desc);
        gp.addRow(2, new Label("Duracao (min)"), dur);
        gp.add(help, 0, 3, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(otId.getText()) != null
                    && trimToNull(desc.getText()) != null
                    && isPositiveInt(dur.getText());
            okBtn.setDisable(!ok);
            help.setText(ok ? "OT, descricao e duracao > 0 obrigatorios." : "Preencha os campos obrigatorios.");
        };
        otId.textProperty().addListener((obs, o, n) -> validate.run());
        desc.textProperty().addListener((obs, o, n) -> validate.run());
        dur.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            try {
                String id = trimToNull(otId.getText());
                String descricao = trimToNull(desc.getText());
                Integer d = parseOptionalInt(dur.getText(), "Duracao");
                if (id == null || descricao == null || d == null) {
                    throw new IllegalArgumentException("Campos obrigatorios em falta");
                }
                serv.adicionarTarefa(id, descricao, d);
                refreshOt();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void addConsumoDialog() {
        OrdemTrabalho sel = otTable.getSelectionModel().getSelectedItem();
        addConsumoDialog(sel == null ? null : sel.getId(), false);
    }

    private void addConsumoDialog(String otIdDefault, boolean lockOtId) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Adicionar consumo de peca");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField otId = new TextField(otIdDefault == null ? "" : otIdDefault);
        if (lockOtId) otId.setDisable(true);
        List<String> skus = new ArrayList<>();
        for (Peca p : serv.listarPecas()) {
            skus.add(p.getSku());
        }
        ComboBox<String> sku = new ComboBox<>(FXCollections.observableArrayList(skus));
        sku.setEditable(true);
        TextField qtd = new TextField();
        Label help = new Label("OT, SKU e quantidade > 0 obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("OT ID"), otId);
        gp.addRow(1, new Label("SKU"), sku);
        gp.addRow(2, new Label("Quantidade"), qtd);
        gp.add(help, 0, 3, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(otId.getText()) != null
                    && trimToNull(comboText(sku)) != null
                    && isPositiveInt(qtd.getText());
            okBtn.setDisable(!ok);
            help.setText(ok ? "OT, SKU e quantidade > 0 obrigatorios." : "Preencha os campos obrigatorios.");
        };
        otId.textProperty().addListener((obs, o, n) -> validate.run());
        sku.valueProperty().addListener((obs, o, n) -> validate.run());
        sku.getEditor().textProperty().addListener((obs, o, n) -> validate.run());
        qtd.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            try {
                String id = trimToNull(otId.getText());
                String skuVal = trimToNull(comboText(sku));
                Integer q = parseOptionalInt(qtd.getText(), "Quantidade");
                if (id == null || skuVal == null || q == null) {
                    throw new IllegalArgumentException("Campos obrigatorios em falta");
                }
                serv.adicionarConsumoPeca(id, skuVal, q);
                refreshOt();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void registarExecucaoDialog() {
        OrdemTrabalho sel = otTable.getSelectionModel().getSelectedItem();
        registarExecucaoDialog(sel == null ? null : sel.getId(), false);
    }

    private void registarExecucaoDialog(String otIdDefault, boolean lockOtId) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Registar execucao");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField otId = new TextField(otIdDefault == null ? "" : otIdDefault);
        if (lockOtId) otId.setDisable(true);
        TextField inicio = new TextField();
        inicio.setPromptText("yyyy-MM-dd HH:mm");
        TextField fim = new TextField();
        fim.setPromptText("yyyy-MM-dd HH:mm");
        TextField causa = new TextField();
        TextField acao = new TextField();
        ComboBox<String> tecnico = new ComboBox<>(FXCollections.observableArrayList(listarTecnicosAtivos()));
        tecnico.setEditable(true);
        TextField obs = new TextField();
        Label help = new Label("OT obrigatoria. Datas devem ser validas se preenchidas.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("OT ID"), otId);
        gp.addRow(1, new Label("Inicio"), inicio);
        gp.addRow(2, new Label("Fim"), fim);
        gp.addRow(3, new Label("Causa"), causa);
        gp.addRow(4, new Label("Acao"), acao);
        gp.addRow(5, new Label("Tecnico"), tecnico);
        gp.addRow(6, new Label("Observacoes"), obs);
        gp.add(help, 0, 7, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(otId.getText()) != null
                    && isValidDateTimeOptional(inicio.getText())
                    && isValidDateTimeOptional(fim.getText());
            okBtn.setDisable(!ok);
            help.setText(ok ? "OT obrigatoria. Datas devem ser validas se preenchidas." : "Preencha OT e datas validas.");
        };
        otId.textProperty().addListener((obs, o, n) -> validate.run());
        inicio.textProperty().addListener((obs, o, n) -> validate.run());
        fim.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            try {
                String id = trimToNull(otId.getText());
                if (id == null) throw new IllegalArgumentException("OT obrigatoria");
                Instant ini = parseOptionalUserDateTime(inicio.getText(), "Inicio");
                Instant fimVal = parseOptionalUserDateTime(fim.getText(), "Fim");
                String causaVal = trimToNull(causa.getText());
                String acaoVal = trimToNull(acao.getText());
                String tecnicoVal = trimToNull(comboText(tecnico));
                String obsVal = trimToNull(obs.getText());
                serv.registarExecucao(id, ini, fimVal, causaVal, acaoVal, tecnicoVal, obsVal);
                refreshOt();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void anexarDocumentoDialog() {
        OrdemTrabalho sel = otTable.getSelectionModel().getSelectedItem();
        anexarDocumentoDialog(sel == null ? null : sel.getId(), false);
    }

    private void anexarDocumentoDialog(String otIdDefault, boolean lockOtId) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Anexar documento");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField otId = new TextField(otIdDefault == null ? "" : otIdDefault);
        if (lockOtId) otId.setDisable(true);
        TextField tipo = new TextField();
        TextField path = new TextField();
        TextField meta = new TextField();
        Label help = new Label("OT, tipo e path obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("OT ID"), otId);
        gp.addRow(1, new Label("Tipo"), tipo);
        gp.addRow(2, new Label("Path"), path);
        gp.addRow(3, new Label("Meta"), meta);
        gp.add(help, 0, 4, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(otId.getText()) != null
                    && trimToNull(tipo.getText()) != null
                    && trimToNull(path.getText()) != null;
            okBtn.setDisable(!ok);
            help.setText(ok ? "OT, tipo e path obrigatorios." : "Preencha OT, tipo e path.");
        };
        otId.textProperty().addListener((obs, o, n) -> validate.run());
        tipo.textProperty().addListener((obs, o, n) -> validate.run());
        path.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            try {
                String id = trimToNull(otId.getText());
                String tipoVal = trimToNull(tipo.getText());
                String pathVal = trimToNull(path.getText());
                if (id == null || tipoVal == null || pathVal == null) {
                    throw new IllegalArgumentException("OT, tipo e path sao obrigatorios");
                }
                String metaVal = trimToNull(meta.getText());
                serv.adicionarDocumento("OT", id, tipoVal, pathVal, metaVal);
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void addPecaDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Adicionar peca");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField sku = new TextField();
        TextField des = new TextField();
        TextField uni = new TextField();
        TextField rep = new TextField();
        TextField custo = new TextField();
        Label help = new Label("SKU, designacao, unidade, reposicao e custo obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("SKU"), sku);
        gp.addRow(1, new Label("Designacao"), des);
        gp.addRow(2, new Label("Unidade"), uni);
        gp.addRow(3, new Label("Reposicao"), rep);
        gp.addRow(4, new Label("Custo unitario"), custo);
        gp.add(help, 0, 5, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(sku.getText()) != null
                    && trimToNull(des.getText()) != null
                    && trimToNull(uni.getText()) != null
                    && isIntInRange(rep.getText(), 0, Integer.MAX_VALUE)
                    && isDoubleValue(custo.getText());
            okBtn.setDisable(!ok);
            help.setText(ok ? "SKU, designacao, unidade, reposicao e custo obrigatorios." : "Revise os campos obrigatorios.");
        };
        sku.textProperty().addListener((obs, o, n) -> validate.run());
        des.textProperty().addListener((obs, o, n) -> validate.run());
        uni.textProperty().addListener((obs, o, n) -> validate.run());
        rep.textProperty().addListener((obs, o, n) -> validate.run());
        custo.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    int r = Integer.parseInt(rep.getText().trim());
                    double c = Double.parseDouble(custo.getText().trim());
                    serv.adicionarPeca(sku.getText().trim(), des.getText().trim(), uni.getText().trim(), r, c);
                    refreshPecas();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void moveStockDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Movimento stock");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField sku = new TextField();
        TextField dep = new TextField("GERAL");
        ComboBox<MovimentoStock.Tipo> tipo = new ComboBox<>(FXCollections.observableArrayList(MovimentoStock.Tipo.values()));
        tipo.getSelectionModel().select(MovimentoStock.Tipo.ENTRADA);
        TextField qtd = new TextField();
        Label help = new Label("SKU, deposito e quantidade > 0 obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("SKU"), sku);
        gp.addRow(1, new Label("Deposito"), dep);
        gp.addRow(2, new Label("Tipo"), tipo);
        gp.addRow(3, new Label("Quantidade"), qtd);
        gp.add(help, 0, 4, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(sku.getText()) != null
                    && trimToNull(dep.getText()) != null
                    && isPositiveInt(qtd.getText());
            okBtn.setDisable(!ok);
            help.setText(ok ? "SKU, deposito e quantidade > 0 obrigatorios." : "Preencha os campos obrigatorios.");
        };
        sku.textProperty().addListener((obs, o, n) -> validate.run());
        dep.textProperty().addListener((obs, o, n) -> validate.run());
        qtd.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    int q = Integer.parseInt(qtd.getText().trim());
                    serv.movimentarStock(sku.getText().trim(), dep.getText().trim(), tipo.getValue(), q, null);
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void inventarioDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Inventario ciclico");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField sku = new TextField();
        TextField dep = new TextField("GERAL");
        TextField qtd = new TextField();
        Label help = new Label("SKU, deposito e quantidade >= 0 obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("SKU"), sku);
        gp.addRow(1, new Label("Deposito"), dep);
        gp.addRow(2, new Label("Quantidade contada"), qtd);
        gp.add(help, 0, 3, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(sku.getText()) != null
                    && trimToNull(dep.getText()) != null
                    && isIntInRange(qtd.getText(), 0, Integer.MAX_VALUE);
            okBtn.setDisable(!ok);
            help.setText(ok ? "SKU, deposito e quantidade >= 0 obrigatorios." : "Preencha os campos obrigatorios.");
        };
        sku.textProperty().addListener((obs, o, n) -> validate.run());
        dep.textProperty().addListener((obs, o, n) -> validate.run());
        qtd.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    int q = Integer.parseInt(qtd.getText().trim());
                    serv.registarInventarioCiclico(sku.getText().trim(), dep.getText().trim(), q);
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void submitPedidoDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Submeter pedido");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField ativo = new TextField();
        TextField desc = new TextField();
        Label help = new Label("Ativo e descricao obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("Ativo"), ativo);
        gp.addRow(1, new Label("Descricao"), desc);
        gp.add(help, 0, 2, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(ativo.getText()) != null && trimToNull(desc.getText()) != null;
            okBtn.setDisable(!ok);
            help.setText(ok ? "Ativo e descricao obrigatorios." : "Preencha ativo e descricao.");
        };
        ativo.textProperty().addListener((obs, o, n) -> validate.run());
        desc.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    serv.submeterPedido(ativo.getText().trim(), desc.getText().trim(), serv.getUtilizadorAtual().getId());
                    refreshPedidos();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void aprovarPedido() {
        if (!requirePerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        PedidoManutencao p = pedidoTable.getSelectionModel().getSelectedItem();
        if (p == null) return;
        try {
            serv.aprovarPedido(p.getId(), serv.getUtilizadorAtual().getId());
            refreshPedidos();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void rejeitarPedido() {
        if (!requirePerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        PedidoManutencao p = pedidoTable.getSelectionModel().getSelectedItem();
        if (p == null) return;
        try {
            serv.rejeitarPedido(p.getId(), serv.getUtilizadorAtual().getId());
            refreshPedidos();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void converterPedido() {
        if (!requirePerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        PedidoManutencao p = pedidoTable.getSelectionModel().getSelectedItem();
        if (p == null) return;
        try {
            serv.converterPedidoEmOt(p.getId(), serv.getUtilizadorAtual().getId());
            refreshPedidos();
            refreshOt();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void addUtilizadorDialog() {
        if (!requirePerfil(Utilizador.Perfil.GESTOR)) return;
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Adicionar utilizador");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField id = new TextField();
        TextField nome = new TextField();
        TextField email = new TextField();
        PasswordField senha = new PasswordField();
        ComboBox<Utilizador.Perfil> perfil = new ComboBox<>(FXCollections.observableArrayList(Utilizador.Perfil.values()));
        perfil.getSelectionModel().select(Utilizador.Perfil.TECNICO);
        Label help = new Label("ID, nome, email, perfil e senha obrigatorios.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("ID"), id);
        gp.addRow(1, new Label("Nome"), nome);
        gp.addRow(2, new Label("Email"), email);
        gp.addRow(3, new Label("Perfil"), perfil);
        gp.addRow(4, new Label("Senha"), senha);
        gp.add(help, 0, 5, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(id.getText()) != null
                    && trimToNull(nome.getText()) != null
                    && trimToNull(email.getText()) != null
                    && trimToNull(senha.getText()) != null
                    && perfil.getValue() != null;
            okBtn.setDisable(!ok);
            help.setText(ok ? "ID, nome, email, perfil e senha obrigatorios." : "Preencha os campos obrigatorios.");
        };
        id.textProperty().addListener((obs, o, n) -> validate.run());
        nome.textProperty().addListener((obs, o, n) -> validate.run());
        email.textProperty().addListener((obs, o, n) -> validate.run());
        senha.textProperty().addListener((obs, o, n) -> validate.run());
        perfil.valueProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    serv.adicionarUtilizador(id.getText().trim(), nome.getText().trim(), email.getText().trim(),
                            perfil.getValue(), senha.getText().trim());
                    refreshUtilizadores();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void inativarUtilizador() {
        if (!requirePerfil(Utilizador.Perfil.GESTOR)) return;
        Utilizador u = utilizadorTable.getSelectionModel().getSelectedItem();
        if (u == null) return;
        try {
            serv.inativarUtilizador(u.getId());
            refreshUtilizadores();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void addFornecedorDialog() {
        if (!requirePerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Adicionar fornecedor");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        TextField nome = new TextField();
        TextField contacto = new TextField();
        TextField email = new TextField();
        TextField sla = new TextField();
        Label help = new Label("Nome obrigatorio. SLA deve ser numero inteiro.");
        help.setStyle("-fx-text-fill: #555555;");
        gp.addRow(0, new Label("Nome"), nome);
        gp.addRow(1, new Label("Contacto"), contacto);
        gp.addRow(2, new Label("Email"), email);
        gp.addRow(3, new Label("SLA (horas)"), sla);
        gp.add(help, 0, 4, 2, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = trimToNull(nome.getText()) != null
                    && (trimToNull(sla.getText()) == null || isIntInRange(sla.getText(), 0, Integer.MAX_VALUE));
            okBtn.setDisable(!ok);
            help.setText(ok ? "Nome obrigatorio. SLA deve ser numero inteiro." : "Preencha nome e SLA valido.");
        };
        nome.textProperty().addListener((obs, o, n) -> validate.run());
        sla.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    Integer s = sla.getText().isBlank() ? null : Integer.parseInt(sla.getText().trim());
                    serv.adicionarFornecedor(nome.getText().trim(), contacto.getText().trim(), email.getText().trim(), s);
                    refreshFornecedores();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private List<Parametro> parametrosPorTipo(Parametro.Tipo tipo) {
        List<Parametro> out = new ArrayList<>();
        for (Parametro p : serv.listarParametros()) {
            if (p.getTipo() == tipo) out.add(p);
        }
        return out;
    }

    private List<String> listarTecnicosAtivos() {
        List<String> out = new ArrayList<>();
        for (Utilizador u : serv.listarUtilizadores()) {
            if (u.getPerfil() == Utilizador.Perfil.TECNICO && u.getEstado() == EstadoUtilizador.ATIVO) {
                out.add(u.getId());
            }
        }
        return out;
    }

    private List<String> listarSkus() {
        List<String> out = new ArrayList<>();
        for (Peca p : serv.listarPecas()) {
            out.add(p.getSku());
        }
        return out;
    }

    private void applyRoleAccess(Tab dashboard, Tab ativos, Tab ots, Tab inventario, Tab pedidos, Tab utilizadores,
                                 Tab fornecedores, Tab auditoria, Tab notificacoes, Tab parametros) {
        Utilizador u = serv.getUtilizadorAtual();
        if (u == null) return;
        Utilizador.Perfil perfil = u.getPerfil();
        boolean gestor = perfil == Utilizador.Perfil.GESTOR;
        boolean planeador = perfil == Utilizador.Perfil.PLANEADOR;
        boolean gestorOuPlaneador = gestor || planeador;

        utilizadores.setDisable(!gestor);
        fornecedores.setDisable(!gestorOuPlaneador);
        auditoria.setDisable(!gestorOuPlaneador);
        parametros.setDisable(!gestorOuPlaneador);
    }

    private boolean requirePerfil(Utilizador.Perfil... perfis) {
        Utilizador u = serv.getUtilizadorAtual();
        if (u == null) return false;
        for (Utilizador.Perfil p : perfis) {
            if (u.getPerfil() == p) return true;
        }
        showError("Permissao insuficiente.");
        return false;
    }

    private String comboText(ComboBox<String> combo) {
        if (combo == null) return null;
        String value = combo.getValue();
        if (value != null && !value.isBlank()) return value.trim();
        String text = combo.getEditor() == null ? null : combo.getEditor().getText();
        return trimToNull(text);
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String t = value.trim();
        return t.isBlank() ? null : t;
    }

    private Integer parseOptionalInt(String value, String label) {
        String t = trimToNull(value);
        if (t == null) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " invalido");
        }
    }

    private Double parseOptionalDouble(String value, String label) {
        String t = trimToNull(value);
        if (t == null) return null;
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " invalido");
        }
    }

    private Instant parseOptionalUserDateTime(String value, String label) {
        String t = trimToNull(value);
        if (t == null) return null;
        try {
            return DateUtils.parseUserDateTime(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(label + " invalido (yyyy-MM-dd HH:mm)");
        }
    }

    private boolean isValidDateTimeOptional(String value) {
        String t = trimToNull(value);
        if (t == null) return true;
        try {
            DateUtils.parseUserDateTime(t);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isIntInRange(String value, int min, int max) {
        String t = trimToNull(value);
        if (t == null) return false;
        try {
            int v = Integer.parseInt(t);
            return v >= min && v <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPositiveInt(String value) {
        String t = trimToNull(value);
        if (t == null) return false;
        try {
            return Integer.parseInt(t) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDoubleValue(String value) {
        String t = trimToNull(value);
        if (t == null) return false;
        try {
            Double.parseDouble(t);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private boolean confirm(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void configurarDashboardDialog() {
        if (!requirePerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Configurar dashboard");

        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);

        ComboBox<Utilizador.Perfil> perfil = new ComboBox<>(FXCollections.observableArrayList(Utilizador.Perfil.values()));
        Utilizador u = serv.getUtilizadorAtual();
        if (u != null) perfil.setValue(u.getPerfil());

        TextField pageSize = new TextField();
        Label help = new Label("Escolha widgets e tamanho de pagina (opcional).");
        help.setStyle("-fx-text-fill: #555555;");

        String[] widgets = new String[] {
                "ATIVOS", "BACKLOG", "MTTR", "MTBF", "PLANOS", "SLA",
                "STOCK", "PEDIDOS", "NOTIFICACOES", "MINHAS_OT"
        };
        GridPane widgetGrid = new GridPane();
        widgetGrid.setHgap(12);
        widgetGrid.setVgap(6);
        List<CheckBox> checks = new ArrayList<>();
        int row = 0;
        int col = 0;
        for (String w : widgets) {
            CheckBox cb = new CheckBox(w);
            checks.add(cb);
            widgetGrid.add(cb, col, row);
            col++;
            if (col == 3) { col = 0; row++; }
        }

        Runnable applyConfig = () -> {
            Utilizador.Perfil p = perfil.getValue();
            DashboardConfig cfg = p == null ? null : serv.obterDashboardPorPerfil(p);
            for (CheckBox cb : checks) cb.setSelected(false);
            if (cfg == null || cfg.getWidgets() == null || cfg.getWidgets().isBlank()) return;
            Set<String> set = new HashSet<>();
            for (String w : cfg.getWidgets().split(",")) {
                String val = w.trim().toUpperCase(Locale.ROOT);
                if (!val.isBlank()) set.add(val);
            }
            for (CheckBox cb : checks) {
                cb.setSelected(set.contains(cb.getText()));
            }
            if (cfg.getPageSize() != null) pageSize.setText(cfg.getPageSize().toString());
        };
        perfil.valueProperty().addListener((obs, o, n) -> applyConfig.run());
        applyConfig.run();

        gp.addRow(0, new Label("Perfil"), perfil);
        gp.addRow(1, new Label("Widgets"), widgetGrid);
        gp.addRow(2, new Label("Tamanho pagina"), pageSize);
        gp.addRow(3, help);

        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        Runnable validate = () -> {
            boolean ok = perfil.getValue() != null
                    && (trimToNull(pageSize.getText()) == null || isIntInRange(pageSize.getText(), 1, 500));
            okBtn.setDisable(!ok);
        };
        perfil.valueProperty().addListener((obs, o, n) -> validate.run());
        pageSize.textProperty().addListener((obs, o, n) -> validate.run());
        validate.run();

        dlg.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            Utilizador.Perfil p = perfil.getValue();
            if (p == null) return;
            List<String> chosen = new ArrayList<>();
            for (CheckBox cb : checks) {
                if (cb.isSelected()) chosen.add(cb.getText());
            }
            String widgetsCsv = String.join(",", chosen);
            Integer size = parseOptionalInt(pageSize.getText(), "Tamanho pagina");
            serv.atualizarDashboard(p, widgetsCsv, size);
            if (u != null && u.getPerfil() == p) {
                loadDashboardWidgets();
                applyDashboardVisibility(kpiChart, backlogChart);
                updateDashboard();
            }
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText("Erro");
        alert.showAndWait();
    }
}

