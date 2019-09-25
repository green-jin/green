<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/cart/addQuickOrder" var="quickOrderAddToCartUrl" htmlEscape="false"/>
<spring:theme code="text.quickOrder.product.quantity.max" var="maxProductQtyMsgHtml"/>
<spring:theme code="text.quickOrder.form.product.exists" var="productExistsInFormMsgHtml"/>
<script>
var ncipProductCode = 0;
</script>
<%-- <div class="js-quick-order-container" data-quick-order-min-rows="${quickOrderMinRows}"
	 data-quick-order-max-rows="${quickOrderMaxRows}" data-quick-order-add-to-cart-url="${quickOrderAddToCartUrl}"
	 data-product-exists-in-form-msg="${productExistsInFormMsgHtml}">

	<ul class="item__list item__list__cart quick-order__list js-ul-container">
		<li class="hidden-xs hidden-sm">
			<ul class="item__list--header">
				<li class="item__sku__input"><spring:theme code="text.quickOrder.page.product"/></li>
				<li class="item__image"></li>
				<li class="item__info"></li>
				<li class="item__price"><spring:theme code="text.quickOrder.page.price"/></li>
				<li class="item__quantity"><spring:theme code="text.quickOrder.page.qty"/></li>
				<li class="item__total--column"><spring:theme code="text.quickOrder.page.total"/></li>
				<li class="item__remove"></li>
			</ul>
		</li>

        <c:forEach begin="1" end="${quickOrderMinRows}">
            <li class="item__list--item js-li-container">
                <div class="item__sku__input js-sku-container">
                    <input type="text" placeholder="Enter SKU" class="js-sku-input-field form-control"/>
                    <input type="hidden" class="js-hidden-sku-field"/>
					<div class="js-sku-validation-container help-block quick-order__help-block"></div>
                </div>
                <div class="item__remove">
                    <button class="btn js-remove-quick-order-row" tabindex="-1">
                        <span class="glyphicon glyphicon-remove"></span>
                    </button>
                </div>
            </li>
        </c:forEach>
	</ul>
</div>
<script id="quickOrderRowTemplate" type="text/x-jquery-tmpl">

	<div class="item__image js-product-info">
		<a href="${request.contextPath}{{= url}}" tabindex="-1">
			{{if images != null && images.length > 0}}
				<img src="{{= images[0].url}}"/>
			{{else}}
				<theme:image code="img.missingProductImage.responsive.thumbnail"/>
			{{/if}}

		</a>
	</div>
	<div class="item__info js-product-info">
		<a href="${request.contextPath}{{= url}}" tabindex="-1">
			<span class="item__name">{{= name}}</span>
		</a>

	   <div class="item__stock">
			<div>
				{{if stock.stockLevelStatus.code && stock.stockLevelStatus.code != 'outOfStock'}}
					<span class="stock">
						<spring:theme code="product.variants.in.stock"/>
					</span>
				{{else}}
					<span class="out-of-stock">
						<spring:theme code="product.variants.out.of.stock"/>
					</span>
				{{/if}}
			</div>
		</div>
	</div>

	<div class="item__price js-product-price js-product-info" data-product-price="{{= price.value}}">
		<span class="visible-xs visible-sm">
			<spring:theme code="basket.page.itemPrice"/>:
		</span>
		{{= price.formattedValue}}
	</div>

	<div class="item__quantity js-product-info">
		
		<input type="text" class="js-quick-order-qty form-control" value="1" maxlength="3" size="1"
			data-max-product-qty="{{= stock.stockLevel}}" data-stock-level-status="{{= stock.stockLevelStatus.code}}"/>
		<div class="js-product-info js-qty-validation-container help-block quick-order__help-block" data-max-product-qty-msg="${maxProductQtyMsgHtml}"></div>
		
	</div>


	<div class="item__total js-product-info js-quick-order-item-total">
		{{if stock.stockLevelStatus.code && stock.stockLevelStatus.code != 'outOfStock'}}
			{{= price.formattedValue}}
		{{/if}}
	</div>
</script> --%>

<div class="js-quick-order-container" data-quick-order-min-rows="${quickOrderMinRows}"
	 data-quick-order-max-rows="${quickOrderMaxRows}" data-quick-order-add-to-cart-url="${quickOrderAddToCartUrl}"
	 data-product-exists-in-form-msg="${productExistsInFormMsgHtml}">

	<ul class="item__list item__list__cart quick-order__list js-ul-container">
		<li class="hidden-xs hidden-sm">
			<div class="d-none d-md-block">
				<div class="row bg-dark p-2">
					<div class="col-3 text-white"><spring:theme code="text.quickOrder.page.product"/></div>
					<div class="col-2 text-white"></div>
					<div class="col-2 text-white"></div>
					<div class="col-1 text-white"><spring:theme code="text.quickOrder.page.price"/></div>
					<div class="col-2 text-white"><spring:theme code="text.quickOrder.page.qty"/></div>
					<div class="col-1 text-white"><spring:theme code="text.quickOrder.page.total"/></div>
					<div class="col-1 text-white"></div>
				</div>
			</div>
		</li>

        <c:forEach begin="1" end="${quickOrderMinRows}">
            <li class="item__list--item js-li-container">
	            <div class="row p-2 no-gutters">
	            	<div class="col-md-3 row d-flex align-content-center mb-2 no-gutters">
		                <div class="col-11">
		                    <input type="text" placeholder="Enter SKU" class="form-control" onchange="test(this.value)"/>
		                    <input type="hidden" class="js-hidden-sku-field"/>
							<div id="findErrorString" class="js-sku-validation-container help-block quick-order__help-block"></div>
		                </div>
	                </div>
	                <div class="item__remove col-md-9 row d-flex justify-content-end no-gutters">
	                	<div class="d-none d-md-block align-self-center no-gutters mr-4">
		                    <button class="btn js-remove-quick-order-row" tabindex="-1">
		                        <span class="glyphicon glyphicon-remove fa fa-times text-primary"></span>
		                    </button>
	                    </div>
	                </div>
	            </div>
            </li>
        </c:forEach>
	</ul>
</div>
<script type="text/javascript">
	function test(x){
		ncipProductCode = x;
		
	}
</script>
<script id="quickOrderRowTemplate" type="text/x-jquery-tmpl">
<div class="col-md-9 row mb-2 no-gutters">
	<div class="col-4 col-md-2 d-flex justify-content-center align-self-center">
	<div class="item__image js-product-info">
		<a href="${request.contextPath}{{= url}}" tabindex="-1">
			{{if images != null && images.length > 0}}
				<img src="{{= images[0].url}}" alt="product img"/>
			{{else}}
				<theme:image code="img.missingProductImage.responsive.thumbnail"/>
			{{/if}}

		</a>
	</div>
	</div>

	<div class="col-8 col-md-10 no-gutters">
	<div class="row no-gutters">
	<div class="col-md-4 align-self-center no-gutters">
	<div class="item__info js-product-info">
		<a href="${request.contextPath}{{= url}}" tabindex="-1">
			<span class="item__name">{{= name}}</span>
		</a>
		
	   <%-- <div class="item__stock">
			<div>
				{{if stock.stockLevelStatus.code && stock.stockLevelStatus.code != 'outOfStock'}}
					<span class="stock">
						<spring:theme code="product.variants.in.stock"/>
					</span>
				{{else}}
					<span class="out-of-stock">
						<spring:theme code="product.variants.out.of.stock"/>
					</span>
				{{/if}}
			</div>
		</div> --%>
	</div>
	</div>
	
	
	<div class="col-md-2 align-self-center no-gutters">
	<div id="price{{= ncipProductCode}}" class="item__price js-product-price js-product-info" data-product-price="{{= price.value}}">
		<span class="visible-xs visible-sm">
			<spring:theme code="basket.page.itemPrice"/>:
		</span>
		{{= price.formattedValue}}
	</div>
	</div>
	<div class="col-md-2 align-self-center no-gutters">
	<div class="btn-group py-3 item__quantity js-product-info" role="group" aria-label="First group">
		<button type="button" class="col-md-1 btn btn-secondary" onclick="minusFunction({{= ncipProductCode}})">-</button>
		<input id="quantityNumber{{= ncipProductCode}}" type="text" class="col-md-5 form-control" value="1" maxlength="3" size="1"
			data-max-product-qty="{{= stock.stockLevel}}" data-stock-level-status="{{= stock.stockLevelStatus.code}}" onchange="totalPrice({{= ncipProductCode}})"/>
		<%-- <div class="js-product-info js-qty-validation-container help-block quick-order__help-block" data-max-product-qty-msg="${maxProductQtyMsgHtml}"></div> --%>
		<button type="button" class="col-md-1 btn btn-secondary" onclick="plusFunction({{= ncipProductCode}})">+</button>
	</div>
	</div>
	
	<div class="col-md-3 align-self-center no-gutters d-flex justify-content-md-end justify-content-start">
	<div id="total{{= ncipProductCode}}" class="item__total js-product-info js-quick-order-item-total">
		{{if stock.stockLevelStatus.code && stock.stockLevelStatus.code != 'outOfStock'}}
			<h6>{{= price.formattedValue}}</h6>
		{{/if}}
	</div>
	</div>
	<div class="col-md-1 d-none d-md-block align-self-center no-gutters text-center"><%-- <i class="fa fa-times text-primary"></i> --%>
	<button class="btn js-remove-quick-order-row" tabindex="-1">
		<span class="glyphicon glyphicon-remove fa fa-times text-primary"></span>
	</button>
	</div>
	</div>
	</div>
</div>
</script>
<script>
	function minusFunction(name){
		var initialNumber = document.getElementById("quantityNumber" + name).value;
		if(initialNumber >= 1){
			document.getElementById("quantityNumber" + name).value = Number(initialNumber) - 1;
			document.getElementById("total" + name).innerText = "$" + Number(document.getElementById("price" + name).getAttribute("data-product-price")) * Number(document.getElementById("quantityNumber" + name).value) + ".00";
		}
	}
</script>
<script>
	function plusFunction(name){
		var initialNumber = document.getElementById("quantityNumber" + name).value;
		var maxQty = (document.getElementById("quantityNumber" + name).getAttribute("data-max-product-qty")) == "" ? "100" : (document.getElementById("quantityNumber" + name).getAttribute("data-max-product-qty"));
		if(initialNumber < Number(maxQty) && Number(maxQty) > 0){
			document.getElementById("quantityNumber" + name).value = Number(initialNumber) + 1;
			document.getElementById("total" + name).innerText = "$" + Number(document.getElementById("price" + name).getAttribute("data-product-price")) * Number(document.getElementById("quantityNumber" + name).value) + ".00";
		}
	}
</script>
<script>
	function totalPrice(name){
		var initialNumber = document.getElementById("quantityNumber" + name).value;
		document.getElementById("total" + name).innerText = "$" + Number(document.getElementById("price" + name).getAttribute("data-product-price")) * Number(document.getElementById("quantityNumber" + name).value) + ".00";
	}
</script>