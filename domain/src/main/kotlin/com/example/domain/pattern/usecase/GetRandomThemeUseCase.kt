package com.example.domain.pattern.usecase

import jakarta.inject.Named

@Named
class GetRandomThemeUseCase () {
    private val themes =  listOf(
        "Aplicativo de delivery de comida",
        "Plataforma de streaming de vídeo",
        "Sistema de reservas de hotel",
        "Loja virtual de roupas",
        "Gerenciador de tarefas diárias",
        "Aplicativo de meditação",
        "Editor de texto online",
        "Plataforma de ensino de idiomas",
        "Sistema de compra de ingressos",
        "Aplicativo de controle financeiro pessoal",
        "Sistema bancário",
        "Aplicativo de gestão de treinos",
        "Sistema de chat em tempo real",
        "App de previsão do tempo",
        "Sistema de agendamento de consultas",
        "Aplicativo de música",
        "Painel de controle de smart home",
        "Sistema de envio de e-mails em massa",
        "Plataforma de leilões online",
        "Aplicativo de carona corporativa",
        "Sistema de votação online",
        "Simulador de investimentos",
        "Plataforma de e-learning com certificações",
        "Sistema de suporte com chatbot",
        "Gerador de relatórios empresariais",
        "Plataforma de concursos públicos",
        "Sistema de rastreamento de encomendas",
        "App de organização de eventos",
        "Simulador de entrevistas de emprego",
        "Plataforma de portfólio para designers",
        "Sistema de gerenciamento de freelancers",
        "Editor de imagens online",
        "Aplicativo de receitas culinárias",
        "Sistema de aluguel de veículos",
        "Plataforma de recomendação de livros",
        "Sistema de inscrição para hackathons",
        "Plataforma de monitoramento de redes sociais",
        "Aplicativo de lembretes por geolocalização",
        "Sistema de monitoramento de sensores IoT",
        "Aplicativo para acompanhamento de gravidez",
        "Sistema de avaliação de desempenho de alunos",
        "Simulador de provas do ENEM",
        "Plataforma de debates online",
        "Sistema de gerenciamento de estoque",
        "Ferramenta de entrevistas técnicas (coding challenges)",
        "Aplicativo de controle de hábitos",
        "Sistema de busca de empregos",
        "Plataforma de trocas de produtos",
        "App para estudo de provas da OAB",
        "Sistema de compartilhamento de documentos",
        "Plataforma de correção de redações"
    )

    fun execute (): Result<String> {
        return runCatching {
            themes.random()
        }
    }
}