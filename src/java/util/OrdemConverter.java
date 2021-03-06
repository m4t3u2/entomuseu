package util;


import dao.OrdemDao;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Ordem;

@FacesConverter(value = "ordemConverter", forClass = Ordem.class)
public class OrdemConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        String nome;
        Ordem temp = null;
        OrdemDao dao = new OrdemDao();
        try {
            nome = value;
            temp = dao.buscarPorNome(nome);
	} catch (Exception e) {
            System.out.println("Erro converter: "+e.toString());
	}
 	return temp;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object obj) {
        if (obj == null){
            return " ";
        }
        if (obj instanceof Ordem){
            Ordem ar = (Ordem)obj;
            return ar.getNome();
        }
        return "";
    }
    
}
