package com.ncip.storefront.controllers.cms;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import org.apache.log4j.Logger;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ncip.core.model.components.HomeSimpleBannerComponentModel;
import com.ncip.storefront.controllers.ControllerConstants;

/**
 * Controller for CMS
 */

@Controller("HomeSimpleBannerComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.HomeSimpleBannerComponent)
public class HomeSimpleBannerComponentController
    extends AbstractAcceleratorCMSComponentController<HomeSimpleBannerComponentModel> {
            
  
  private static final Logger LOG = Logger.getLogger(HomeSimpleBannerComponentController.class);

  @Resource(name = "responsiveMediaFacade")
  private ResponsiveMediaFacade responsiveMediaFacade;

  @Resource(name = "commerceCommonI18NService")
  private CommerceCommonI18NService commerceCommonI18NService;

  @Override
  protected void fillModel(HttpServletRequest request, Model model,
      HomeSimpleBannerComponentModel component) {
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("=== HomeSimpleBannerComponentController.fillModel  start ==="+ControllerConstants.Actions.Cms.HomeSimpleBannerComponent);
    }
     
    final List<ImageData> mediaDataList = responsiveMediaFacade.getImagesFromMediaContainer(
        component.getMedia(commerceCommonI18NService.getCurrentLocale()));
    
    model.addAttribute("component", component);
    model.addAttribute("medias", mediaDataList);
    model.addAttribute("urlLink", component.getUrlLink());
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("=== HomeSimpleBannerComponentController.commerceCommonI18NService.getCurrentLocale() ==="+ commerceCommonI18NService.getCurrentLocale());
      LOG.debug("=== HomeSimpleBannerComponentController.fillModel  end ===");
    } 
  }
}
