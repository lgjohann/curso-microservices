package com.johann.msavaliadorcredito.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johann.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartoes;

    public void solicitarCartao(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        String json = convertIntoJson(dados);
        rabbitTemplate.convertAndSend(queueEmissaoCartoes.getName(), json);
    }

    public String convertIntoJson(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dados);
        return json;

    }

}
