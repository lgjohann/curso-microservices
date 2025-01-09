package com.johann.msavaliadorcredito.application;

import com.johann.msavaliadorcredito.application.ex.DadosClientesNotFoundException;
import com.johann.msavaliadorcredito.application.ex.ErroComunicacaoMicroserviceException;
import com.johann.msavaliadorcredito.domain.model.CartaoCliente;
import com.johann.msavaliadorcredito.domain.model.DadosCliente;
import com.johann.msavaliadorcredito.domain.model.SituacaoCliente;
import com.johann.msavaliadorcredito.infra.clients.CartoesResourceClient;
import com.johann.msavaliadorcredito.infra.clients.ClienteResourceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClientesNotFoundException, ErroComunicacaoMicroserviceException {
        try{
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> dadosCartaoResponse = cartoesClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(dadosCartaoResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (status == 404) {
                throw new DadosClientesNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(e.getMessage(), status);
        }


    }
}
