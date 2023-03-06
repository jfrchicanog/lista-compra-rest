package es.uma.informatica.sii.helloworld.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.uma.informatica.sii.helloworld.dto.ItemDTO;
import es.uma.informatica.sii.helloworld.dto.ListaCompraDTO;
import es.uma.informatica.sii.helloworld.entities.Item;
import es.uma.informatica.sii.helloworld.entities.ListaCompra;
import es.uma.informatica.sii.helloworld.service.ListaCompraDBService;
import es.uma.informatica.sii.helloworld.service.NoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/listas")
public class ListaCompraController {
	public static final String LISTA_COMPRA_PATH="/listas"; 
	public static final String ITEM_PATH="/item";
	
	private ListaCompraDBService service;
	private ModelMapper modelMapper;
	
	@Autowired
	public ListaCompraController(ListaCompraDBService service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping
	public List<Long> obtieneListas() {
		return service.todasListascompras();
	}
	
	@PostMapping
	public ResponseEntity<?> aniadeLista(@RequestBody ListaCompraDTO nuevaLista, UriComponentsBuilder builder) {
		ListaCompra lc = modelMapper.map(nuevaLista, ListaCompra.class);
		lc.setId(null);
		Long id = service.aniadirListaCompra(lc);
		URI uri = builder.path("/listas")
						.path(String.format("/%d", id))
						.build()
						.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ListaCompraDTO> obtenerLista(@PathVariable(name = "id") Long id) {
		Optional<ListaCompra> listaCompraById = service.listaCompraById(id);
		return ResponseEntity.of(listaCompraById
				.map(l->modelMapper.map(l, ListaCompraDTO.class)));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<?> modificaLista(@PathVariable(name = "id") Long id, @RequestBody ListaCompraDTO lista) {		
		ListaCompra lc = modelMapper.map(lista, ListaCompra.class);
		if (service.listaCompraById(id).isPresent()) {
			lc.setId(id);
			service.modificarListaCompra(lc);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> eliminarLista(@PathVariable(name = "id") Long id) {
		if (service.listaCompraById(id).isPresent()) {
			service.eliminarListaCompra(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("{id}")
	public ResponseEntity<?> aniadeItem(@PathVariable(name="id") Long idLista, @RequestBody ItemDTO item, UriComponentsBuilder uriBuilder) {
		Item itemEntity = modelMapper.map(item, Item.class);
		itemEntity.setId(null);
		Long idItem = service.aniadirItem(idLista, itemEntity);
		URI uri = uriBuilder
				.path(LISTA_COMPRA_PATH).path(String.format("/%d", idLista))
				.path(ITEM_PATH).path(String.format("/%d", idItem))
				.build()
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("{idLista}/item/{idItem}")
	public ResponseEntity<ItemDTO> obtenerItem(@PathVariable(name="idLista") Long idLista, @PathVariable(name="idItem") Long idItem) {
		Optional<ItemDTO> item = service.getItem(idLista, idItem)
				.map(it->modelMapper.map(it, ItemDTO.class));
		return ResponseEntity.of(item);
	}
	
	
	@PutMapping("{idLista}/item/{idItem}")
	public ResponseEntity<?> modificarItem(@PathVariable(name="idLista") Long idLista, @PathVariable(name="idItem") Long idItem, @RequestBody ItemDTO item) {
		if (service.getItem(idLista, idItem).isPresent()) {
			Item itemEntity = modelMapper.map(item, Item.class);
			itemEntity.setId(idItem);
			service.modificarItem(idLista, itemEntity);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("{idLista}/item/{idItem}")
	public ResponseEntity<?> eliminarItem(@PathVariable(name="idLista") Long idLista, @PathVariable(name="idItem") Long idItem) {
		service.eliminarItem(idLista, idItem);
		return ResponseEntity.ok().build();
	}
	
	@ExceptionHandler(NoEncontradoException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void noEncontrado() {
		
	}

}
