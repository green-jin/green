package com.ncip.facades.populators;

import com.ncip.facades.product.data.FileData;
import com.ncip.facades.product.data.VideoData;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class VideoDataPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
    AbstractProductPopulator<SOURCE, TARGET> {

  private String videoFormats;
  private String pdfFormats;
  private String dwgFormats;

  public String getVideoFormats() {
    return videoFormats;
  }

  public void setVideoFormats(String videoFormats) {
    this.videoFormats = videoFormats;
  }

  public String getPdfFormats() {
    return pdfFormats;
  }

  public void setPdfFormats(String pdfFormats) {
    this.pdfFormats = pdfFormats;
  }

  public String getDwgFormats() {
    return dwgFormats;
  }

  public void setDwgFormats(String dwgFormats) {
    this.dwgFormats = dwgFormats;
  }

  @Override
  public void populate(final SOURCE productModel, final TARGET productData)
      throws ConversionException {

    final List<MediaContainerModel> mediaContainers = new ArrayList<MediaContainerModel>();
    collectMediaContainers(productModel, mediaContainers);

    if (!mediaContainers.isEmpty()) {
      final List<VideoData> videoList = new ArrayList<VideoData>();
      final List<FileData> fileList = new ArrayList<FileData>();

      // fill our video list with the product's existing media
      //Video
      if (productData.getVideos() != null) {
        videoList.addAll(productData.getVideos());
      }
      //PDF or DWG
      if(productData.getFiles() != null){
        fileList.addAll(productData.getFiles());
      }

      // Use all the videos as gallery videos
      for (final MediaContainerModel mediaContainer : mediaContainers) {
        addDataByMediaFormats(productModel,mediaContainer, videoList, fileList);
      }

      if (!videoList.isEmpty()) {
        productData.setVideos(videoList);
      }

      if(!fileList.isEmpty()){
        productData.setFiles(fileList);
      }
    }

  }


  protected void collectMediaContainers(final ProductModel productModel,
      final List<MediaContainerModel> list) {
    final List<MediaContainerModel> galleryMedias = (List<MediaContainerModel>) getProductAttribute(
        productModel,ProductModel.GALLERYIMAGES);  //GALLERYIMAGES is MediaContainerList

    if (galleryMedias != null) {
      for (final MediaContainerModel galleryMedia : galleryMedias) {
        if (!list.contains(galleryMedia)) {
          list.add(galleryMedia);
        }
      }

      if (galleryMedias.isEmpty() && productModel instanceof VariantProductModel) {
        collectMediaContainers(((VariantProductModel) productModel).getBaseProduct(), list);
      }
    }
  }


  protected void addDataByMediaFormats(ProductModel productModel, final MediaContainerModel mediaContainer, final List<VideoData> Videolist, final List<FileData> FileList) {

    Collection<MediaModel> medias = mediaContainer.getMedias();

    if (medias != null) {
      for (MediaModel media : medias) {
        final MediaFormatModel mediaFormat = media.getMediaFormat();
        if (mediaFormat.getQualifier().equals(getVideoFormats())) { //辨別media是否為video, getVideoFormats: video

          VideoData videoData = new VideoData();
          videoData.setUrl(media.getURL());
          videoData.setFormat(media.getMediaFormat().getQualifier());
          if(media.getAltText() == null){
            videoData.setAltText(productModel.getName());
          }else{
            videoData.setAltText(media.getAltText());
          }
          Videolist.add(videoData);

        }else if(mediaFormat.getQualifier().equals(getPdfFormats()) || mediaFormat.getQualifier().equals(getDwgFormats())){

          FileData fileData = new FileData();
          fileData.setUrl((media.getURL()));
          fileData.setFormat(media.getMediaFormat().getQualifier());
          fileData.setFileItems(media.getFileItem());
          if(media.getAltText() == null){
            fileData.setAltText(productModel.getName());
          }else{
            fileData.setAltText(media.getAltText());
          }

          FileList.add(fileData);
        }

      }

    }
  }

}
