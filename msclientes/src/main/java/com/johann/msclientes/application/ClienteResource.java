package com.johann.msclientes.application;

import com.johann.msclientes.application.representation.ClienteSaveRequest;
import com.johann.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteResource {

    private final ClienteService clienteService;

    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservice de clientes: ");
        return "ok";
    }

    @PostMapping
    public ResponseEntity<Cliente> ClienteSaveRequest(@RequestBody ClienteSaveRequest request) {
        Cliente cliente = request.toModel();
        clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public  ResponseEntity<Cliente> dadosCliente(@RequestParam String cpf) {
        Optional<Cliente> cliente = clienteService.getByCPF(cpf);
        if(cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente.get());
    }
}
