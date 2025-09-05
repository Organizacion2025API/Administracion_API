package org.esfe.ApiApexManagent.config;

import org.esfe.ApiApexManagent.dtos.categoria.CategoriaGuardar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaModificar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaSalida;
import org.esfe.ApiApexManagent.modelos.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapperConfig {
    
    public CategoriaSalida toDto(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        
        return new CategoriaSalida(
            categoria.getId(),
            categoria.getNombreCategoria()
        );
    }
    
    public Categoria toEntity(CategoriaGuardar categoriaGuardar) {
        if (categoriaGuardar == null) {
            return null;
        }
        
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(categoriaGuardar.getNombreCategoria());
        return categoria;
    }
    
    public Categoria toEntity(CategoriaModificar categoriaModificar, Categoria categoria) {
        if (categoriaModificar == null || categoria == null) {
            return null;
        }
        
        categoria.setNombreCategoria(categoriaModificar.getNombreCategoria());
        return categoria;
    }
}