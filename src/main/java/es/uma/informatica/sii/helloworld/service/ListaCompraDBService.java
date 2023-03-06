package es.uma.informatica.sii.helloworld.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uma.informatica.sii.helloworld.entities.Item;
import es.uma.informatica.sii.helloworld.entities.ListaCompra;
import es.uma.informatica.sii.helloworld.repositories.ItemRepository;
import es.uma.informatica.sii.helloworld.repositories.ListaCompraRepository;



@Service
@Transactional
public class ListaCompraDBService {

	private ListaCompraRepository listaRepository;
	private ItemRepository itemRepository;

	@Autowired
	public ListaCompraDBService(ListaCompraRepository repo, ItemRepository itemRepository) {
		this.listaRepository = repo;
		this.itemRepository = itemRepository;
	}

	public Optional<ListaCompra> listaCompraById(Long id) {
		return listaRepository.findById(id);
	}

	public List<Long> todasListascompras() {
		return StreamSupport.stream(listaRepository.findAll().spliterator(),false)
				.map(ListaCompra::getId).toList();
	}

	public Long aniadirListaCompra(ListaCompra lc) {
		listaRepository.save(lc);
		return lc.getId();
	}

	public void modificarListaCompra(ListaCompra lista) {
		if (listaRepository.existsById(lista.getId())) {
			listaRepository.findById(lista.getId())
			.ifPresent(l->l.setNombre(lista.getNombre()));
		} else {
			throw new NoEncontradoException();
		}
	}

	public void eliminarListaCompra(Long id) {
		if (listaRepository.existsById(id)) {
			listaRepository.deleteById(id);
		} else {
			throw new NoEncontradoException();
		}
	}

	public Long aniadirItem(Long idLista, Item item) {
		var lista = listaRepository.findById(idLista);
		if (lista.isPresent()) {
			item = itemRepository.save(item);

			ListaCompra lc = lista.get();
			lc.getItems().add(item);
			return item.getId();			
		} else {
			return null;
		}
	}

	public void modificarItem(Long idLista, Item item) {
		if (itemRepository.existsById(item.getId())) {
			itemRepository.save(item);
		} else {
			throw new NoEncontradoException();
		}
	}

	public void eliminarItem(Long idLista, Long idItem) {
		if (itemRepository.existsById(idItem)) {
			itemRepository.deleteById(idItem);
		} else {
			throw new NoEncontradoException();
		}
	}

	public Optional<Item> getItem(Long idLista, Long idItem) {
		return itemRepository.findById(idItem);
	}


}
