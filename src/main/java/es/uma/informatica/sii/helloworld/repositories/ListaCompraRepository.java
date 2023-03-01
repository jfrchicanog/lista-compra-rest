package es.uma.informatica.sii.helloworld.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uma.informatica.sii.helloworld.entities.ListaCompra;

@Repository
public interface ListaCompraRepository extends CrudRepository<ListaCompra, Long> {

}
