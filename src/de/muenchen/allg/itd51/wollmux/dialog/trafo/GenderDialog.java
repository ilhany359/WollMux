/*
 * Dateiname: GenderDialog.java
 * Projekt  : WollMux
 * Funktion : Erlaubt die Bearbeitung der Funktion eines Gender-Feldes.
 *
 * Copyright (c) 2008-2019 Landeshauptstadt München
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the European Union Public Licence (EUPL),
 * version 1.0 (or any later version).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 *
 * You should have received a copy of the European Union Public Licence
 * along with this program. If not, see
 * http://ec.europa.eu/idabc/en/document/7330
 *
 * Änderungshistorie:
 * Datum      | Wer | Änderungsgrund
 * -------------------------------------------------------------------
 * 21.02.2008 | LUT | Erstellung als GenderDialog
 * -------------------------------------------------------------------
 *
 * Christoph lutz (D-III-ITD 5.1)
 * @version 1.0
 *
 */
package de.muenchen.allg.itd51.wollmux.dialog.trafo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.awt.XButton;
import com.sun.star.awt.XComboBox;
import com.sun.star.awt.XContainerWindowProvider;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XDialog;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowPeer;
import com.sun.star.uno.UnoRuntime;

import de.muenchen.allg.afid.UNO;
import de.muenchen.allg.itd51.wollmux.core.dialog.adapter.AbstractActionListener;
import de.muenchen.allg.itd51.wollmux.core.parser.ConfigThingy;
import de.muenchen.allg.itd51.wollmux.document.TextDocumentController;
import de.muenchen.allg.itd51.wollmux.dialog.trafo.GenderTrafoModel;

import de.muenchen.allg.itd51.wollmux.core.exceptions.UnavailableException;

/**
 * Erlaubt die Bearbeitung der Funktion eines Gender-Feldes.
 *
 * @author Christoph Lutz (D-III-ITD 5.1)
 */
public class GenderDialog
{
  private static final Logger LOGGER = LoggerFactory.getLogger(GenderDialog.class);

  private XControlContainer controlContainer;

  private XDialog dialog;

  private TextDocumentController documentController;
  
  private GenderTrafoModel model;

  public GenderDialog(List<String> fieldNames, TextDocumentController documentController, GenderTrafoModel model)
  {
    this.documentController = documentController;
    this.model = model;

    HashSet<String> uniqueFieldNames = new HashSet<>(fieldNames);
    List<String> sortedNames = new ArrayList<>(uniqueFieldNames);
    Collections.sort(sortedNames);

    buildGUI(sortedNames);
  }

  /**
   * Baut das genderPanel auf.
   */
  private void buildGUI(final List<String> fieldNames)
  {
    try
    {
      XWindowPeer peer = UNO.XWindowPeer(UNO.desktop.getCurrentFrame().getContainerWindow());
      XContainerWindowProvider provider = UnoRuntime.queryInterface(XContainerWindowProvider.class,
          UNO.xMCF.createInstanceWithContext("com.sun.star.awt.ContainerWindowProvider",
              UNO.defaultContext));

      XWindow window = provider.createContainerWindow(
          "vnd.sun.star.script:WollMux.gender_dialog?location=application", "", peer, null);
      controlContainer = UnoRuntime.queryInterface(XControlContainer.class, window);

      XComboBox cbAnrede = UNO.XComboBox(controlContainer.getControl("cbSerienbrieffeld"));
      cbAnrede.addItems(fieldNames.toArray(new String[fieldNames.size()]), (short) 0);

      XButton btnAbort = UNO.XButton(controlContainer.getControl("btnAbort"));
      btnAbort.addActionListener(btnAbortActionListener);

      XButton btnOK = UNO.XButton(controlContainer.getControl("btnOK"));
      btnOK.addActionListener(btnOKActionListener);

      dialog = UnoRuntime.queryInterface(XDialog.class, window);
      dialog.execute();
    } catch (com.sun.star.uno.Exception e)
    {
      LOGGER.error("", e);
    }
  }

  private AbstractActionListener btnAbortActionListener = event -> dialog.endExecute();

  private AbstractActionListener btnOKActionListener = event -> {
    ConfigThingy conf = model.generateGenderTrafoConf();
    try
    {
      if (model.getFunctionName() == null)
      {
        documentController.replaceSelectionWithTrafoField(conf, "Gender");
      } else
      {
        documentController.setTrafo(model.getFunctionName(), conf);
      }
    } catch ( UnavailableException ex)
    {
      LOGGER.debug("", ex);
    }
     
    dialog.endExecute();
  };
}
