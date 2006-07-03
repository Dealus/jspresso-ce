/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.backend.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractBackendAction;
import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.util.url.UrlHelper;

/**
 * Generates a jasper report.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class GenerateJasperReportAction extends AbstractBackendAction {

  private JdbcTemplate jdbcTemplate;

  /**
   * {@inheritDoc}
   */
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    try {
      IReport reportDesign = (IReport) context
          .get(ActionContextConstants.ACTION_PARAM);
      final JasperReport jasperReport = JasperCompileManager
          .compileReport(UrlHelper.createURL(
              reportDesign.getReportDescriptor().getReportDesignUrl())
              .openStream());
      final Map<String, Object> reportContext = new HashMap<String, Object>(
          reportDesign.getContext());
      reportContext.putAll(context);
      JasperPrint jasperPrint = (JasperPrint) jdbcTemplate
          .execute(new ConnectionCallback() {

            public Object doInConnection(Connection con) {
              try {
                return JasperFillManager.fillReport(jasperReport,
                    reportContext, con);
              } catch (JRException ex) {
                throw new ActionException(ex);
              }
            }
          });
      context.put(ActionContextConstants.ACTION_PARAM, jasperPrint);
    } catch (JRException ex) {
      throw new ActionException(ex);
    } catch (IOException ex) {
      throw new ActionException(ex);
    }
    return true;
  }

  /**
   * Sets the jdbcTemplate.
   * 
   * @param jdbcTemplate
   *          the jdbcTemplate to set.
   */
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

}
