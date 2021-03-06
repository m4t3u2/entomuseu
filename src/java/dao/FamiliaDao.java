package dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import modelo.Familia;
import modelo.Ordem;
import util.JpaUtil;

public class FamiliaDao implements Serializable {
    
    public Familia buscarPorNome(String nome){
        Familia temp;
        EntityManager manager = JpaUtil.getEntityManager();
        TypedQuery<Familia> query = manager.createNamedQuery("Familia.listarPorNome", Familia.class);
        query.setParameter("nome", nome);
        temp = query.getSingleResult();
        manager.close();
        return temp;
    }
    
    
    public boolean inserir(Familia fam){
        EntityManager manager = JpaUtil.getEntityManager();
        EntityTransaction tx = manager.getTransaction(); 
        tx.begin();
        manager.persist(fam);
        tx.commit();
        manager.close();
        return true;
    } 
    
    public List<Familia> listarFamilia(){
        EntityManager manager = JpaUtil.getEntityManager();
        TypedQuery<Familia> query = manager.createNamedQuery("Familia.listarTodos", Familia.class);
        List<Familia> lista = query.getResultList();
        manager.close();
        return lista;
    }
    
    public List<Familia> listarPorOrdem(Ordem ordem){
        EntityManager manager = JpaUtil.getEntityManager();
        TypedQuery<Familia> query = manager.createNamedQuery("Familia.buscarPorOrdem", Familia.class);
        query.setParameter("codigo", ordem.getCodigo());
        List<Familia> lista = query.getResultList();
        manager.close();
        return lista;     
    }
}
