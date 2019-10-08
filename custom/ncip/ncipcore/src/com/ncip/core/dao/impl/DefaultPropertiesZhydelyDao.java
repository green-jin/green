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
import com.ncip.core.constants.NcipCoreConstants;
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
      "INSERT INTO SAPABAP1.ZHYDELY(VKORG,VBELN_D,VBELN,POSNR,MATNR,LFIMG,MEINS,WADAT_IST,LGORT,LFSTK,DELY_TYPE,LFDAT,CRT_DATE,FRM_SYS,TO_SYS,ICTYPE,STATUS,MANDT) "
          + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
  private static final String IS_EXISTED =
      "SELECT count(1) FROM SAPABAP1.ZHYDELY t WHERE t.VKORG=:VKORG AND t.VBELN_D=:VBELN_D AND t.POSNR=:POSNR AND t.MANDT=:MANDT";
  private static final String UPDATE_BY_MODEL =
      "UPDATE SAPABAP1.ZHYDELY t\r\n" + "SET\r\n" + "t.VKORG=?,\r\n" + "t.VBELN_D=?,\r\n" + "t.VBELN=?,\r\n"
          + "t.POSNR=?,\r\n" + "t.MATNR=?,\r\n" + "t.LFIMG=?,\r\n" + "t.MEINS=?,\r\n"
          + "t.WADAT_IST=?,\r\n" + "t.LGORT=?,\r\n" + "t.LFSTK=?,\r\n" + "t.DELY_TYPE=?,\r\n"
          + "t.LFDAT=?,\r\n" + "t.CRT_DATE=?,\r\n" + "t.FRM_SYS=?,\r\n" + "t.TO_SYS=?,\r\n"
          + "t.ICTYPE=?,\r\n" + "t.STATUS=?\r\n"
          + "WHERE t.VKORG=? AND t.VBELN_D=? AND t.POSNR=? AND t.MANDT=?";
  private static final String ZERO_STOCK_CONSIGNMENT_SQL = "SELECT * FROM SAPABAP1.ZHYDELY t "
      + "WHERE 1=1 "
      + " AND t.MANDT=:MANDT "
      + " AND t.STATUS='" + ConsignmentSyncConstants.SAP_ZHYDELY_STATUS_UNPROCESSED
      + "' AND t.DELY_TYPE in (" + ConsignmentSyncConstants.ZERO_STOCK_2_CONSIGNMENT_DELY_TYPE
      + ") AND t.FRM_SYS='" + ConsignmentSyncConstants.SAP + "'  AND t.TO_SYS='"
      + ConsignmentSyncConstants.HYBRIS + "'";
  private static final String UPDATE_STATUS =
      "UPDATE SAPABAP1.ZHYDELY t SET t.STATUS=:status WHERE t.VBELN_D=:code AND t.MANDT=:MANDT";

  @Override
  public void insertByModel(final ZhydelyBean model) {

    LOG.info("Insert By Model. SQL=" + INSERT_BY_MODEL);

    jdbcTemplate = new JdbcTemplate(getDataSource());

    final Object[] modelParams = new Object[] {model.getVkorg(), model.getVbeln_d(),
        model.getVbeln(), model.getPosnr(), model.getMatnr(), model.getLfimg(), model.getMeins(),
        model.getWadat_ist(), model.getLgort(), model.getLfstk(), model.getDely_type(),
        model.getLfdat(), model.getCrt_date(), model.getFrm_sys(), model.getTo_sys(),
        model.getIctype(), model.getStatus(), model.getMandt()};
    jdbcTemplate.update(INSERT_BY_MODEL, modelParams);

  }

  @Override
  public boolean isExistedInZTable(final String org, final String code, final Long entryNum) {

    LOG.info("Check if it existed in ZTable. SQL=" + IS_EXISTED);

    namedParamJDBCTemplate = new NamedParameterJdbcTemplate(getDataSource());

    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("VKORG", org);
    namedParameters.addValue("VBELN_D", code);
    namedParameters.addValue("POSNR", entryNum);
    namedParameters.addValue("MANDT", getManDtValue());

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
            model.getStatus(), model.getVkorg(), model.getVbeln_d(), model.getPosnr(),
            model.getMandt()};
    jdbcTemplate.update(UPDATE_BY_MODEL, modelParams);

  }

  @Override
  public List<ZhydelyBean> queryForZeroStockDistSync() {

    namedParamJDBCTemplate = new NamedParameterJdbcTemplate(getDataSource());

    LOG.info("QUERY SQL=" + ZERO_STOCK_CONSIGNMENT_SQL);

    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("MANDT", getManDtValue());

    final List<ZhydelyBean> list =
        namedParamJDBCTemplate.query(ZERO_STOCK_CONSIGNMENT_SQL, namedParameters,
            new ZhydelyRowMapper());

    return list;
  }

  @Override
  public void updateStatus(final String code, final String status) {

    LOG.info("Update ZTable status. SQL=" + UPDATE_STATUS);

    namedParamJDBCTemplate = new NamedParameterJdbcTemplate(getDataSource());

    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("code", code);
    namedParameters.addValue("status", status);
    namedParameters.addValue("MANDT", getManDtValue());

    namedParamJDBCTemplate.update(UPDATE_STATUS, namedParameters);

  }

  /* This DataSource is for local test only. Replace it with others in PROD. */
  private DataSource getDataSource() {
    final DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName(
        configurationService.getConfiguration().getString(NcipCoreConstants.JDBC_DRIVERCLASSNAME));
    ds.setUrl(configurationService.getConfiguration().getString(NcipCoreConstants.JDBC_URL));
    ds.setUsername(
        configurationService.getConfiguration().getString(NcipCoreConstants.JDBC_USERNAME));
    ds.setPassword(
        configurationService.getConfiguration().getString(NcipCoreConstants.JDBC_PASSWORD));
    return ds;
  }

  /* obtain ERP_MANDT value via local properties */
  private String getManDtValue() {
    return configurationService.getConfiguration().getString(NcipCoreConstants.ERP_MANDT);
  }

}
