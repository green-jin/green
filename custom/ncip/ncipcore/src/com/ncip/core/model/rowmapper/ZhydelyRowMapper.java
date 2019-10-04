package com.ncip.core.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.ncip.core.model.ZhydelyBean;

public class ZhydelyRowMapper implements RowMapper<ZhydelyBean> {

  @Override
  public ZhydelyBean mapRow(final ResultSet rs, final int rowNum) throws SQLException {

    final ZhydelyBean bean = new ZhydelyBean();
    bean.setVkorg(rs.getString("VKORG"));
    bean.setVbeln_d(rs.getString("VBELN_D"));
    bean.setVbeln(rs.getString("VBELN"));
    bean.setPosnr(rs.getLong("POSNR"));
    bean.setMatnr(rs.getString("MATNR"));
    bean.setLfimg(rs.getLong("LFIMG"));
    bean.setMeins(rs.getString("MEINS"));
    bean.setWadat_ist(rs.getDate("WADAT_IST"));
    bean.setLgort(rs.getString("LGORT"));
    bean.setLfstk(rs.getString("LFSTK"));
    bean.setDely_type(rs.getString("DELY_TYPE"));
    bean.setLfdat(rs.getDate("LFDAT"));
    bean.setCrt_date(rs.getString("CRT_DATE"));
    bean.setFrm_sys(rs.getString("FRM_SYS"));
    bean.setTo_sys(rs.getString("TO_SYS"));
    bean.setIctype(rs.getString("ICTYPE"));
    bean.setStatus(rs.getString("STATUS"));

    return bean;
  }

}
