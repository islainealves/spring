package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Pessoa;
import com.example.demo.repository.PessoaRepository;
import com.example.demo.responses.Response;

// A anotação @RestController permite definir um controller com características API REST;
@RestController
public class PessoaController {
	// A anotação @Autowired delega ao Spring Boot a inicialização do objeto;
    @Autowired
    private PessoaRepository pessoaRepository;
    // A anotação @RequestMapping permite definir uma rota
    @RequestMapping(value = "/pessoa", method = RequestMethod.GET)
    public List<Pessoa> Get() {
        return pessoaRepository.findAll();
    }
    
    // @PathVariable indica que o valor da variável virá de uma informação da rota;
    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.GET)
    public ResponseEntity<Pessoa> GetById(@PathVariable(value = "id") long id)
    {
    	// https://docs.oracle.com/javase/9/docs/api/java/util/Optional.html (desde v 1.8)
    	// findById espera um retorno do tipo Optional<Pessoa>
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if(pessoa.isPresent())
            return new ResponseEntity<Pessoa>(pessoa.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // @RequestBody indica que o valor do objeto virá do corpo da requisição e 
    //              consegue mapear os dados vindos em Json para os atributos da classe;
    @RequestMapping(value = "/pessoa", method =  RequestMethod.POST)
    public ResponseEntity<Response<Pessoa>> Post(@Valid @RequestBody Pessoa pessoa, BindingResult result)
    {
    	Response<Pessoa> response = new Response<Pessoa>();    	
    	if (result.hasErrors()) {
    		result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
    		return ResponseEntity.badRequest().body(response);
    	}
    	pessoaRepository.save(pessoa);
    	response.setData(pessoa);
        return ResponseEntity.ok(response);
    }        

    @RequestMapping(value = "/pessoa/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<Response<Pessoa>> Put(@PathVariable(value = "id") long id, @Valid @RequestBody 
    											Pessoa newPessoa, BindingResult result)
    {
        Optional<Pessoa> oldPessoa = pessoaRepository.findById(id);
    	Response<Pessoa> response = new Response<Pessoa>();    	
    	if (result.hasErrors()) {
    		result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
    		return ResponseEntity.badRequest().body(response);
    	}        	        
        if(oldPessoa.isPresent()){
            Pessoa pessoa = oldPessoa.get();
            pessoa.setName(newPessoa.getName());
            response.setData(pessoa);
            pessoaRepository.save(pessoa);
            return ResponseEntity.ok(response);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }    
    
    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id)
    {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if(pessoa.isPresent()){
            pessoaRepository.delete(pessoa.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }    
    
}	