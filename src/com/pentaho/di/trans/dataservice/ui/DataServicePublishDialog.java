/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2014 Pentaho Corporation (Pentaho). All rights reserved.
 *
 * NOTICE: All information including source code contained herein is, and
 * remains the sole property of Pentaho and its licensors. The intellectual
 * and technical concepts contained herein are proprietary and confidential
 * to, and are trade secrets of Pentaho and may be covered by U.S. and foreign
 * patents, or patents in process, and are protected by trade secret and
 * copyright laws. The receipt or possession of this source code and/or related
 * information does not convey or imply any rights to reproduce, disclose or
 * distribute its contents, or to manufacture, use, or sell anything that it
 * may describe, in whole or in part. Any reproduction, modification, distribution,
 * or public display of this information without the express written authorization
 * from Pentaho is strictly prohibited and in violation of applicable laws and
 * international treaties. Access to the source code contained herein is strictly
 * prohibited to anyone except those individuals and entities who have executed
 * confidentiality and non-disclosure agreements or other agreements with Pentaho,
 * explicitly covering such access.
 */

package com.pentaho.di.trans.dataservice.ui;

import com.pentaho.di.trans.dataservice.DataServiceMeta;
import com.pentaho.di.trans.dataservice.ui.controller.DataServicePublishController;
import com.pentaho.di.trans.dataservice.ui.model.DataServicePublishModel;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.dom.Document;
import org.pentaho.ui.xul.swt.SwtXulLoader;
import org.pentaho.ui.xul.swt.SwtXulRunner;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class DataServicePublishDialog implements java.io.Serializable {
  private static final String XUL_PATH = "com/pentaho/di/trans/dataservice/ui/xul/dataservice-publish-dialog.xul";
  private static final String XUL_DIALOG_ID = "dataservice-publish-dialog";

  private final DataServicePublishModel model = new DataServicePublishModel();
  private final DataServicePublishController dataServicePublishController;
  private final Document xulDocument;
  private final XulDialog dialog;

  private static final Class<?> CLZ = DataServicePublishDialog.class;
  private final ResourceBundle resourceBundle = new ResourceBundle() {
    @Override
    public Enumeration<String> getKeys() {
      return null;
    }

    @Override
    protected Object handleGetObject( String key ) {
      return BaseMessages.getString( CLZ, key );
    }
  };

  public DataServicePublishDialog( Composite parent, DataServiceMeta dataService,
                                TransMeta transMeta ) throws KettleException {
    dataServicePublishController = new DataServicePublishController( model, dataService, transMeta );
    xulDocument = initXul( parent );
    dialog = (XulDialog) xulDocument.getElementById( XUL_DIALOG_ID );
    attachCallback();
  }

  public void open( ) throws KettleException {
    dialog.show();
  }

  public void close() {
    dialog.hide();
  }

  private void attachCallback() {
    dataServicePublishController.setCallback(
      new DataServicePublishCallback() {
        @Override
        public void onClose() {
          close();
        }
      }
    );
  }

  private Document initXul( Composite parent ) throws KettleException {
    try {
      SwtXulLoader swtLoader = new SwtXulLoader();
      swtLoader.setOuterContext( parent );
      swtLoader.registerClassLoader( getClass().getClassLoader() );
      XulDomContainer container = swtLoader.loadXul( XUL_PATH, resourceBundle );
      container.addEventHandler( dataServicePublishController );

      final XulRunner runner = new SwtXulRunner();
      runner.addContainer( container );
      runner.initialize();
      return container.getDocumentRoot();
    } catch ( XulException xulException ) {
      throw new KettleException( "Failed to initialize DataServicePublishDialog.",
        xulException );
    }
  }
}