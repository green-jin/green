package com.ncip.core.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import com.ncip.core.constants.ConsignmentSyncConstants;
import com.ncip.core.dao.ZhydelyDao;
import com.ncip.core.model.ZhydelyBean;
import com.ncip.core.model.rowmapper.ZhydelyRowMapper;
import de.hybris.platform.servicelayer.config.ConfigurationService;

@Component(value = "zhydelyDao")
public class DefaultPropertiesZhydelyDao implements ZhydelyDao {

  private static final Logger LOG = Logger.getLogger(DefaultPropertiesZhydelyDao.class);

  @Resource
  private ConfigurationService configurationService;
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedParamJDBCTemplate;

  private static final String INSERT_BY_MODEL =
      "INSERT INTO ZHYDELY VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  private static final String IS_EXISTED =
      "SELECT count(1) FROM ZHYDELY t WHERE t.VKORG=:VKORG AND t.VBELN_D=:VBELN_D AND t.POSNR=:POSNR";
  private static final String UPDATE_BY_MODEL =
      "UPDATE ZHYDELY t\r\n" + "SET\r\n" + "t.VKORG=?,\r\n" + "t.VBELN_D=?,\r\n" + "t.VBELN=?,\r\n"
          + "t.POSNR=?,\r\n" + "t.MATNR=?,\r\n" + "t.LFIMG=?,\r\n" + "t.MEINS=?,\r\n"
          + "t.WADAT_IST=?,\r\n" + "t.LGORT=?,\r\n" + "t.LFSTK=?,\r\n" + "t.DELY_TYPE=?,\r\n"
          + "t.LFDAT=?,\r\n" + "t.CRT_DATE=?,\r\n" + "t.FRM_SYS=?,\r\n" + "t.TO_SYS=?,\r\n"
          + "t.ICTYPE=?,\r\n" + "t.STATUS=?\r\n" + "WHERE t.VKORG=? AND t.VBELN_D=? AND t.POSNR=?";
  private static final String ZERO_STOCK_CONSIGNMENT_SQL = "SELECT * FROM ZHYDELY t " + "WHERE 1=1 "
      + " AND t.STATUS='" + ConsignmentSyncConstants.SAP_ZHYDELY_STATUS_UNPROCESSED
      + "' AND t.DELY_TYPE in (" + ConsignmentSyncConstants.ZERO_STOCK_2_CONSIGNMENT_DELY_TYPE
      + ") AND t.FRM_SYS='" + ConsignmentSyncConstants.SAP + "'  AND t.TO_SYS='"
      + ConsignmentSyncConstants.HYBRIS + "'";
  private static final String UPDATE_STATUS =
      "UPDATE ZHYDELY t SET t.STATUS=:status WHERE t.VBELN_D=:code";

  @Override
  public void insertByModel(final ZhydelyBean model) {

    LOG.info("ZTable Start");

    jdbcTemplate = new JdbcTemplate(getDataSource());

    final Object[] modelParams = new Object[] {model.getVkorg(), model.getVbeln_d(),
        model.getVbeln(), model.getPosnr(), model.getMatnr(), model.getLfimg(), model.getMeins(),
        model.getWadat_ist(), model.getLgort(), model.getLfstk(), model.getDely_type(),
        model.getLfdat(), model.getCrt_date(), model.getFrm_sys(), model.getTo_sys(),
        model.getIctype(), model.getStatus()};
    jdbcTemplate.update(INSERT_BY_MODEL, modelParams);

    LOG.info("ZTable End");

  }

  @Override
  public boolean isExistedInZTable(final String org, final String code, final Long entryNum) {

    namedParamJDBCTemplate = new NamedParameterJdbcTemplate(getDataSource());

    // jdbcTemplate.queryForObject(IS_EXISTED, Long.class);
    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("VKORG", org);
    namedParameters.addValue("VBELN_D", code);
    namedParameters.addValue("POSNR", entryNum);

    final Long count =
        namedParamJDBCTemplate.queryForObject(IS_EXISTED, namedParameters, Long.class);

    if (count > 0) {
      return true;
    } else {
      return false;
    }

  }

  @Override
  public void updateByModel(final ZhydelyBean model) {

    jdbcTemplate = new JdbcTemplate(getDataSource());

    final Object[] modelParams =
        new Object[] {model.getVkorg(), model.getVbeln_d(), model.getVbeln(), model.getPosnr(),
            model.getMatnr(), model.getLfimg(), model.getMeins(), model.getWadat_ist(),
            model.getLgort(), model.getLfstk(), model.getDely_type(), model.getLfdat(),
            model.getCrt_date(), model.getFrm_sys(), model.getTo_sys(), model.getIctype(),
            model.getStatus(), model.getVkorg(), model.getVbeln_d(), model.getPosnr()};
    jdbcTemplate.update(UPDATE_BY_MODEL, modelParams);

  }

  @Override
  public List<ZhydelyBean> queryForZeroStockDistSync() {

    namedParamJDBCTemplate = new NamedParameterJdbcTemplate(getDataSource());

    LOG.info("SQL=" + ZERO_STOCK_CONSIGNMENT_SQL);

    final List<ZhydelyBean> list =
        namedParamJDBCTemplate.query(ZERO_STOCK_CONSIGNMENT_SQL, new ZhydelyRowMapper());

    return list;
  }

  @Override
  public void updateStatus(final String code, final String status) {

    namedParamJDBCTemplate = new NamedParameterJdbcTemplate(getDataSource());

    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("code", code);
    namedParameters.addValue("status", status);

    namedParamJDBCTemplate.update(UPDATE_STATUS, namedParameters);

  }

  /* DataSource for local test only. */
  private DataSource getDataSource() {
    final DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName(
        configurationService.getConfiguration().getString("jdbc.driverClassName"));
    ds.setUrl(configurationService.getConfiguration().getString("jdbc.url"));
    ds.setUsername(configurationService.getConfiguration().getString("jdbc.username"));
    ds.setPassword(configurationService.getConfiguration().getString("jdbc.password"));
    return ds;
  }

}
