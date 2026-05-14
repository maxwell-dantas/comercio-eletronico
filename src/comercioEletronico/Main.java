package comercioEletronico;

import comercioEletronico.template.visitante.VisitanteTemplate;
import comercioEletronico.view.visitante.VisitanteView;

public class Main {
    public static void main(String[] args) {
        VisitanteView.inicializarSistema();
        VisitanteTemplate.menuLogin();
    }
}