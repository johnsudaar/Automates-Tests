import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.ClassicPickSupport;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Permet la visualisation d'un automate
 * Cet outil de visualisation est loin d'être complet et/ou finalisé, il est ici uniquement pour du deboggage.
 * (Rouge : Etat final, Gros : Etat initial)
 * Faiblesses connues :
 *  - Lent
 *  - Impossible d'avoir un ':' sur un arc
 *  - Très très Hacky
 *  - Les edge labels sont mal placés sur les arcs mono-noeud
 * Created by johnsudaar on 25/10/15.
 */

public class AutomateViewer {
    public AutomateViewer(Automate aut, String title){

        Graph<String, String> g = AutomateViewer.buildGraph(aut);

        Layout<String, String> layout = new CircleLayout<String, String>(g);
        layout.setSize(new Dimension(300,300));
        VisualizationViewer<String,String> vv = new VisualizationViewer<String,String>(layout);
        vv.setPreferredSize(new Dimension(350,350));

        // Mouse support
        vv.setPickSupport(new ClassicPickSupport<String, String>());
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);

        vv.getRenderContext().setLabelOffset(20);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        vv.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);


        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<String, String>() {
            @Override
            public String transform(String c) {
                return c.split(":")[0];
            }
        });

        vv.getRenderContext().setVertexLabelTransformer(new Transformer<String, String>() {
            @Override
            public String transform(String c) {
                return c.split(":")[0];
            }
        });

        Transformer<String,Paint> vertexColor = new Transformer<String,Paint>() {
            public Paint transform(String i) {
                if(i.contains("F")) return Color.GREEN;
                return Color.WHITE;
            }
        };

        Transformer<String,Shape> vertexSize = new Transformer<String,Shape>(){
            public Shape transform(String i){
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
                // in this case, the vertex is twice as large
                if(i.contains("I")) return AffineTransform.getScaleInstance(1.5, 1.5).createTransformedShape(circle);
                else return circle;
            }
        };

        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);


        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    public static Graph buildGraph(Automate aut) {
        Graph<String, String> g = new DirectedSparseMultigraph<String, String>();
        String[] corresp = new String[aut.size()];
        // Ajout des noeuds
        for(int i = 0; i < aut.size(); i++) {
            corresp[i] = ""+i;
            if(aut.getInitialNode() == i) {
                corresp[i] = corresp[i]+":I";
            }
            if(aut.isAcceptant(i)) {
                corresp[i]=corresp[i]+":F";
            }
            g.addVertex(corresp[i]);
        }

        // Ajout des arcs

        for(int x = 0; x < aut.size(); x++) {
            for(int y= 0 ; y < aut.size(); y++) {
                ArrayList<Character> chars = aut.lien(x, y);
                String s = "";
                for(char c : chars) {
                    s += c+",";
                }
                if(s.length() != 0) {
                    s=s.substring(0,s.length() - 1)+":"+x+":"+y;
                    if(x==y) {
                        s= "                        "+s; // PAS BEAU
                    }
                    g.addEdge(s, corresp[x], corresp[y]);
                }
            }
        }
        return g;
    }
}
