package de.muenchen.allg.itd51.wollmux.former.insertion;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import de.muenchen.allg.itd51.wollmux.core.functions.FunctionLibrary;
import de.muenchen.allg.itd51.wollmux.former.view.View;
import de.muenchen.allg.itd51.wollmux.former.view.ViewChangeListener;

/**
 * Anzeige erweiterter Eigenschaften eines InsertionModels.
 * 
 * @author Matthias Benkmann (D-III-ITD 5.1)
 */
public class OneInsertionExtView implements View
{
  /**
   * Typischerweise ein Container, der die View enthält und daher über Änderungen auf
   * dem Laufenden gehalten werden muss.
   */
  private ViewChangeListener bigDaddy;

  /**
   * Die oberste Komponente dieser View.
   */
  private JTabbedPane myTabbedPane;

  /**
   * Das Model zu dieser View.
   */
  private InsertionModel model;

  /**
   * Erzeugt eine neue View.
   * 
   * @param model
   *          das Model dessen Daten angezeigt werden sollen.
   * @param funcLib
   *          die Funktionsbibliothek deren Funktionen zur Verfügung gestellt werden
   *          sollen für das Auswählen von Attributen, die eine Funktion erfordern.
   * @param myViewChangeListener
   *          typischerweise ein Container, der diese View enthält und über
   *          Änderungen informiert werden soll.
   */
  public OneInsertionExtView(InsertionModel model, FunctionLibrary funcLib,
      ViewChangeListener myViewChangeListener)
  {
    this.bigDaddy = myViewChangeListener;
    this.model = model;
    myTabbedPane = new JTabbedPane();

    // als ViewChangeListener wird null übergeben, weil die OneInsertionExtView sich
    // nachher
    // direkt auf dem Model als Listener registriert.
    OneInsertionTrafoView trafoView =
      new OneInsertionTrafoView(model, funcLib, null);
    myTabbedPane.addTab("TRAFO", trafoView.getComponent());

    model.addListener(new MyModelChangeListener());
  }

  public JComponent getComponent()
  {
    return myTabbedPane;
  }

  /**
   * Liefert das Model zu dieser View.
   */
  public InsertionModel getModel()
  {
    return model;
  }

  private class MyModelChangeListener implements InsertionModel.ModelChangeListener
  {
    public void modelRemoved(InsertionModel model)
    {
      if (bigDaddy != null) bigDaddy.viewShouldBeRemoved(OneInsertionExtView.this);
    }

    public void attributeChanged(InsertionModel model, int attributeId,
        Object newValue)
    {}
  }

}
