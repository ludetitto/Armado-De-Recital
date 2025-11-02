package test;

import com.igormaznitsa.jprol.data.Term;
import com.igormaznitsa.jprol.libs.JProlCoreLibrary;
import com.igormaznitsa.jprol.logic.JProlChoicePoint;
import com.igormaznitsa.jprol.logic.JProlContext;

public class PrologTests {
    public static void main(String[] args) throws Exception {
        final String reglas = """
            roles_cubiertos(CantAsignados, GuitAsignados, CantRequeridos, GuitRequeridos) :-
                CantAsignados >= CantRequeridos,
                GuitAsignados >= GuitRequeridos.
            miembro(X, [X|_]).
            miembro(X, [_|T]) :- miembro(X, T).
        """;

        final JProlContext contexto = new JProlContext("musica", new JProlCoreLibrary());
        contexto.consult(new java.io.StringReader(reglas));

        final String consulta1 = "roles_cubiertos(1, 1, 1, 1).";
        final JProlChoicePoint cp1 = new JProlChoicePoint(consulta1, contexto);
        boolean resultado1 = cp1.prove() != null;
        System.out.println("[Caso 1] 1 cantante y 1 guitarrista requeridos, se asignan 1 y 1.");
        System.out.println("→ ¿Están cubiertos los roles? " + (resultado1 ? "✅ SÍ" : "❌ NO"));
        System.out.println();

        final String consulta2 = "roles_cubiertos(0, 1, 1, 1).";
        final JProlChoicePoint cp2 = new JProlChoicePoint(consulta2, contexto);
        boolean resultado2 = cp2.prove() != null;
        System.out.println("[Caso 2] Se requieren 1 cantante y 1 guitarrista, pero solo hay 0 y 1.");
        System.out.println("→ ¿Están cubiertos los roles? " + (resultado2 ? "✅ SÍ" : "❌ NO"));
        System.out.println();

        String consulta3 = "X is 2, roles_cubiertos(1, X, 1, 1), Res = X.";
        JProlChoicePoint cp3 = new JProlChoicePoint(consulta3, contexto);
        while (cp3.prove() != null) {
            Term valorX = cp3.findVar("Res").get().getValue();
            System.out.println("[Caso 3] Se liga la variable X con valor 2.");
            System.out.println("Consulta: roles_cubiertos(1, X, 1, 1).");
            System.out.println("→ Valor de X evaluado: " + valorX.toSrcString());
            System.out.println("→ Resultado: ✅ roles cubiertos\n");
        }

        String consulta4 = "miembro(X, [0,1,2,3,4,5]), roles_cubiertos(1, X, 1, 1), Res = X.";
        JProlChoicePoint cp4 = new JProlChoicePoint(consulta4, contexto);
        System.out.println("[Caso 4] Generar X en [0,1,2,3,4,5] y filtrar los que cumplen roles_cubiertos(1,X,1,1):");
        while (cp4.prove() != null) {
            Term valorX = cp4.findVar("Res").get().getValue();
            System.out.println("→ X = " + valorX.toSrcString() + " cumple los requerimientos");
        }
        System.out.println();

        int cantAsignados = 2;
        int guitAsignados = 1;
        int cantReq = 1;
        int guitReq = 1;

        final String consultaParam = String.format(
                "roles_cubiertos(%d, %d, %d, %d).",
                cantAsignados, guitAsignados, cantReq, guitReq
        );
        JProlChoicePoint cpParam = new JProlChoicePoint(consultaParam, contexto);
        boolean resultadoParam = cpParam.prove() != null;
        System.out.println("[Caso 5] Consulta parametrizada desde Java:");
        System.out.println("  Cantantes asignados: " + cantAsignados);
        System.out.println("  Guitarristas asignados: " + guitAsignados);
        System.out.println("  Requeridos: " + cantReq + " y " + guitReq);
        System.out.println("→ ¿Están cubiertos los roles? " + (resultadoParam ? "✅ SÍ" : "❌ NO"));
    }
}
