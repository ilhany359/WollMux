package de.muenchen.allg.itd51.wollmux.former.group;

import de.muenchen.allg.itd51.wollmux.core.functions.FunctionLibrary;
import de.muenchen.allg.itd51.wollmux.core.util.L;
import de.muenchen.allg.itd51.wollmux.former.BroadcastListener;
import de.muenchen.allg.itd51.wollmux.former.BroadcastObjectSelection;
import de.muenchen.allg.itd51.wollmux.former.FormularMax4kController;
import de.muenchen.allg.itd51.wollmux.former.view.OnDemandCardView;
import de.muenchen.allg.itd51.wollmux.former.view.View;
import de.muenchen.allg.itd51.wollmux.former.view.ViewChangeListener;

/**
 * Eine View, die alle
 * {@link de.muenchen.allg.itd51.wollmux.former.group.OneGroupFuncView}s enthält.
 * 
 * @author Matthias Benkmann (D-III-ITD 5.1)
 */
public class AllGroupFuncViewsPanel extends OnDemandCardView
{

  /**
   * Die Funktionsbibliothek, die die Funktionen enthält, die die Views zur Auswahl
   * anbieten sollen.
   */
  private FunctionLibrary funcLib;

  /**
   * Erzeugt ein {@link AllGroupFuncViewsPanel}, das den Inhalt von groupModelList
   * anzeigt.
   * 
   * @param funcLib
   *          die Funktionsbibliothek, die die Funktionen enthält, die die Views zur
   *          Auswahl anbieten sollen.
   */
  public AllGroupFuncViewsPanel(GroupModelList groupModelList,
      FunctionLibrary funcLib, FormularMax4kController formularMax4000)
  {
    super(L.m("Function-View"));
    this.funcLib = funcLib;
    groupModelList.addListener(new MyItemListener());
    formularMax4000.addBroadcastListener(new MyBroadcastListener());

    for (GroupModel model : groupModelList)
      if (model.hasFunc()) addItem(model);
  }

  @Override
  public View createViewFor(Object model, ViewChangeListener viewChangeListener)
  {
    GroupModel m = (GroupModel) model;
    return new OneGroupFuncView(m, funcLib, viewChangeListener);
  }

  private class MyItemListener implements GroupModelList.ItemListener
  {
    @Override
    public void itemAdded(GroupModel model, int index)
    {
      if (model.hasFunc()) addItem(model);
    }

    @Override
    public void itemRemoved(GroupModel model, int index)
    {
    // Hier muss nicht removeItem(model) aufgerufen werden. Dies behandelt die
    // OnDemandCardView selbst mittels Listener
    }
  }

  private class MyBroadcastListener extends BroadcastListener
  {
    @Override
    public void broadcastFormControlModelSelection(BroadcastObjectSelection b)
    {
      showEmpty();
    }

    @Override
    public void broadcastGroupModelSelection(BroadcastObjectSelection b)
    {
      if (b.getState() == 1)
      {
        show(b.getObject());
      }
      else
      {
        showEmpty();
      }
    }
  }

}
