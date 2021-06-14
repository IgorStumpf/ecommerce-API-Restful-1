package com.residencia.ecommerce.controllers;

import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.residencia.ecommerce.exception.EmailException;
import com.residencia.ecommerce.repositories.ProdutoRepository;
import com.residencia.ecommerce.services.ClienteService;
import com.residencia.ecommerce.services.PedidoService;
import com.residencia.ecommerce.services.ProdutoService;
import com.residencia.ecommerce.vo.CadastrarNovoPedidoVO;
import com.residencia.ecommerce.vo.PedidoVO;
import com.residencia.ecommerce.vo.Views.PedidoClienteView;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

	@Autowired
	PedidoService pedidoService;
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	ProdutoRepository produtoRepository;

	@GetMapping("/{id}")
	public ResponseEntity<PedidoClienteView> findById(@PathVariable Integer id) {
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<>(pedidoService.findById(id), headers, HttpStatus.OK);
	}
	
	@GetMapping("/meus-pedido")
	public ResponseEntity<List<PedidoClienteView>> findMyInfo() {
		HttpHeaders headers = new HttpHeaders();
		
		List<PedidoClienteView> lPedidosCliente = pedidoService.findMyPedidos(clienteService.getCliente());
		
		if (lPedidosCliente != null) {
			return new ResponseEntity<>(lPedidosCliente, headers, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(lPedidosCliente, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/listar-todos")
	public ResponseEntity<List<PedidoClienteView>> findAllView(
			@RequestParam(required = false) Integer pagina,
			@RequestParam(required = false) Integer qtdRegistros) 
					throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<>(pedidoService.findAllView(pagina, 
				qtdRegistros), headers, HttpStatus.OK);
	}

	@GetMapping("/count")
	public Long count() {
		return pedidoService.count();
		//IGOR E MARCOS
	}

	@PostMapping
	public ResponseEntity<PedidoVO> save(@RequestBody PedidoVO pedidoVO) {

		HttpHeaders headers = new HttpHeaders();

		PedidoVO novoPedidoVO = pedidoService.save(pedidoVO);

		if (null != novoPedidoVO)
			return new ResponseEntity<>(novoPedidoVO, headers, HttpStatus.OK);
		else
			return new ResponseEntity<>(novoPedidoVO, headers, HttpStatus.BAD_REQUEST);
	}
	
	
	@PostMapping("/novo-pedido")
	public ResponseEntity<String> novoPedido(@Valid @RequestBody CadastrarNovoPedidoVO cadastrarNovoPedidoVO) throws MessagingException, EmailException {

		HttpHeaders headers = new HttpHeaders();

		
		
		if (produtoRepository.findByNome(cadastrarNovoPedidoVO.getNomeProduto()) != null) {
			
			PedidoClienteView novoPedidoVO = pedidoService.novoPedido(cadastrarNovoPedidoVO);
			
			if (null != novoPedidoVO) {
				return new ResponseEntity<>("Pedido Realizado com Sucesso!\n Numero do seu pedido: " + novoPedidoVO.getNumeroDoPedido() + "\n NotaFiscal Enviada para seu e-mail"  , headers, HttpStatus.OK);
			}
			
			else {
				return new ResponseEntity<>("Insira os Dados corretos", headers, HttpStatus.BAD_REQUEST);
			}
			
		}
		else {
			
			return new ResponseEntity<>("Insira um produto existente", headers, HttpStatus.BAD_REQUEST);
			
		}
		
	}
	

	@PutMapping("/{id}")
	public PedidoVO update(@RequestBody PedidoVO pedidoVO, Integer id) {
		return pedidoService.update(pedidoVO, id);
	}

	@DeleteMapping("/{id}")
	public void DeleteById(@PathVariable Integer id) {
		pedidoService.delete(id);
	}
}
