/**
 *
 */
package com.ncip.core.model;

import java.util.Date;


/**
 * @author user
 *
 */
public class ZhydelyBean {

  private String vkorg;
  private String vbeln_d;
  private String vbeln;
  private Long posnr;
  private String matnr;
  private Long lfimg;
  private String meins;
  private Date wadat_ist;
  private String lgort;
  private String lfstk;
  private String dely_type;
  private Date arrivalDate;
  private Date lfdat;
  private String logisticsStatus;
  private String crt_date;
  private String frm_sys;
  private String to_sys;
  private String ictype;
  private String status;
  private String mandt;

  /**
   * @return the vkorg
   */
  public String getVkorg() {
    return vkorg;
  }

  /**
   * @param vkorg the vkorg to set
   */
  public void setVkorg(final String vkorg) {
    this.vkorg = vkorg;
  }

  /**
   * @return the vbeln_d
   */
  public String getVbeln_d() {
    return vbeln_d;
  }

  /**
   * @param vbeln_d the vbeln_d to set
   */
  public void setVbeln_d(final String vbeln_d) {
    this.vbeln_d = vbeln_d;
  }

  /**
   * @return the vbeln
   */
  public String getVbeln() {
    return vbeln;
  }

  /**
   * @param vbeln the vbeln to set
   */
  public void setVbeln(final String vbeln) {
    this.vbeln = vbeln;
  }

  /**
   * @return the posnr
   */
  public Long getPosnr() {
    return posnr;
  }

  /**
   * @param posnr the posnr to set
   */
  public void setPosnr(final Long posnr) {
    this.posnr = posnr;
  }

  /**
   * @return the matnr
   */
  public String getMatnr() {
    return matnr;
  }

  /**
   * @param matnr the matnr to set
   */
  public void setMatnr(final String matnr) {
    this.matnr = matnr;
  }

  /**
   * @return the lfimg
   */
  public Long getLfimg() {
    return lfimg;
  }

  /**
   * @param lfimg the lfimg to set
   */
  public void setLfimg(final Long lfimg) {
    this.lfimg = lfimg;
  }

  /**
   * @return the meins
   */
  public String getMeins() {
    return meins;
  }

  /**
   * @param meins the meins to set
   */
  public void setMeins(final String meins) {
    this.meins = meins;
  }

  /**
   * @return the wadat_ist
   */
  public Date getWadat_ist() {
    return wadat_ist;
  }

  /**
   * @param wadat_ist the wadat_ist to set
   */
  public void setWadat_ist(final Date wadat_ist) {
    this.wadat_ist = wadat_ist;
  }

  /**
   * @return the lgort
   */
  public String getLgort() {
    return lgort;
  }

  /**
   * @param lgort the lgort to set
   */
  public void setLgort(final String lgort) {
    this.lgort = lgort;
  }

  /**
   * @return the lfstk
   */
  public String getLfstk() {
    return lfstk;
  }

  /**
   * @param lfstk the lfstk to set
   */
  public void setLfstk(final String lfstk) {
    this.lfstk = lfstk;
  }

  /**
   * @return the dely_type
   */
  public String getDely_type() {
    return dely_type;
  }

  /**
   * @param dely_type the dely_type to set
   */
  public void setDely_type(final String dely_type) {
    this.dely_type = dely_type;
  }

  /**
   * @return the arrivalDate
   */
  public Date getArrivalDate() {
    return arrivalDate;
  }

  /**
   * @param arrivalDate the arrivalDate to set
   */
  public void setArrivalDate(final Date arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  /**
   * @return the lfdat
   */
  public Date getLfdat() {
    return lfdat;
  }

  /**
   * @param lfdat the lfdat to set
   */
  public void setLfdat(final Date lfdat) {
    this.lfdat = lfdat;
  }

  /**
   * @return the logisticsStatus
   */
  public String getLogisticsStatus() {
    return logisticsStatus;
  }

  /**
   * @param logisticsStatus the logisticsStatus to set
   */
  public void setLogisticsStatus(final String logisticsStatus) {
    this.logisticsStatus = logisticsStatus;
  }

  /**
   * @return the crt_date
   */
  public String getCrt_date() {
    return crt_date;
  }

  /**
   * @param crt_date the crt_date to set
   */
  public void setCrt_date(final String crt_date) {
    this.crt_date = crt_date;
  }

  /**
   * @return the frm_sys
   */
  public String getFrm_sys() {
    return frm_sys;
  }

  /**
   * @param frm_sys the frm_sys to set
   */
  public void setFrm_sys(final String frm_sys) {
    this.frm_sys = frm_sys;
  }

  /**
   * @return the to_sys
   */
  public String getTo_sys() {
    return to_sys;
  }

  /**
   * @param to_sys the to_sys to set
   */
  public void setTo_sys(final String to_sys) {
    this.to_sys = to_sys;
  }

  /**
   * @return the ictype
   */
  public String getIctype() {
    return ictype;
  }

  /**
   * @param ictype the ictype to set
   */
  public void setIctype(final String ictype) {
    this.ictype = ictype;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(final String status) {
    this.status = status;
  }

  public String getMandt() {
    return mandt;
  }

  public void setMandt(final String mandt) {
    this.mandt = mandt;
  }

  @Override
  public String toString() {
    return "ZhydelyBean [vkorg=" + vkorg + ", vbeln_d=" + vbeln_d + ", vbeln=" + vbeln + ", posnr="
        + posnr + ", matnr=" + matnr + ", lfimg=" + lfimg + ", meins=" + meins + ", wadat_ist="
        + wadat_ist + ", lgort=" + lgort + ", lfstk=" + lfstk + ", dely_type=" + dely_type
        + ", arrivalDate=" + arrivalDate + ", lfdat=" + lfdat + ", logisticsStatus="
        + logisticsStatus + ", crt_date=" + crt_date + ", frm_sys=" + frm_sys + ", to_sys=" + to_sys
        + ", ictype=" + ictype + ", status=" + status + ", mandt=" + mandt + "]";
  }

}
