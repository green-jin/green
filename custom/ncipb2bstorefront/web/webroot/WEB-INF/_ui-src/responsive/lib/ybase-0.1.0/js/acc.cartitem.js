
ACC.cartitem = {

	_autoload: [
		"bindCartItem"
	],

	submitTriggered: false,

	bindCartItem: function ()
	{

		$('.js-execute-entry-action-button').on("click", function ()
		{
			var entryAction = $(this).data("entryAction");
			var entryActionUrl =  $(this).data("entryActionUrl");
			var entryProductCode =  $(this).data("entryProductCode");
			var entryInitialQuantity =  $(this).data("entryInitialQuantity");
			var actionEntryNumbers =  $(this).data("actionEntryNumbers");

			if(entryAction == 'REMOVE')
			{
				ACC.track.trackRemoveFromCart(entryProductCode, entryInitialQuantity);
			}

			var cartEntryActionForm = $("#cartEntryActionForm");
			var entryNumbers = actionEntryNumbers.toString().split(';');
			entryNumbers.forEach(function(entryNumber) {
				var entryNumbersInput = $("<input>").attr("type", "hidden").attr("name", "entryNumbers").val(entryNumber);
				cartEntryActionForm.append($(entryNumbersInput));
			});
			cartEntryActionForm.attr('action', entryActionUrl).submit();
		});

		// 2019/09/26 plus
		$('#button_plus').on("click", function (e)
		{
			ACC.cartitem.handleUpdateQuantity_plus(this, e);
		});

		// 2019/09/26 minus
		$('#button_minus').on("click", function (e)
		{
			ACC.cartitem.handleUpdateQuantity_minus(this, e);
		});
		
		$('.js-update-entry-quantity-input').on("blur", function (e)
		{
			return ACC.cartitem.handleUpdateQuantity(this, e);

		}).on("keyup", function (e)
		{
			return ACC.cartitem.handleKeyEvent(this, e);
		}).on("keydown", function (e)
		{
			return ACC.cartitem.handleKeyEvent(this, e);
		}
		);
	},

	handleKeyEvent: function (elementRef, event)
	{
		//console.log("key event (type|value): " + event.type + "|" + event.which);
	
		if (event.which == 13 && !ACC.cartitem.submitTriggered)
		{
			ACC.cartitem.submitTriggered = ACC.cartitem.handleUpdateQuantity(elementRef, event);
			
			return false;
		}
		else 
		{
			// Ignore all key events once submit was triggered
			if (ACC.cartitem.submitTriggered)
			{
				return false;
			}
		}

		return true;
	},

	handleUpdateQuantity: function (elementRef, event)
	{

		var form = $(elementRef).closest('form');

		var productCode = form.find('input[name=productCode]').val();
		var initialCartQuantity = form.find('input[name=initialQuantity]').val();
		var newCartQuantity = form.find('input[name=quantity]').val();
		
		if(initialCartQuantity != newCartQuantity)
		{
			ACC.track.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
			
			form.submit();

			return true;
		}

		return false;
	},
	
	// for quantity button plus
	handleUpdateQuantity_plus: function (elementRef, event)
	{

		var form = $(elementRef).closest('form');

		var productCode = form.find('input[name=productCode]').val();
		var initialCartQuantity = form.find('input[name=initialQuantity]').val();
		var newCartQuantity = form.find('input[name=quantity]').val();
		var entryNumber = form.find('input[name=entryNumber]').val();
		var entryId =  '#quantity_' + entryNumber;
		$(entryId).val(parseInt(newCartQuantity) + parseInt('1'));
		
		
		ACC.track.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
			
	    form.submit();

	    return true;
	}, 
	
	// for quantity button minus
	handleUpdateQuantity_minus: function (elementRef, event)
	{

		var form = $(elementRef).closest('form');

		var productCode = form.find('input[name=productCode]').val();
		var initialCartQuantity = form.find('input[name=initialQuantity]').val();
		var newCartQuantity = form.find('input[name=quantity]').val();
		var entryNumber = form.find('input[name=entryNumber]').val();
		var entryId =  '#quantity_' + entryNumber;
		$(entryId).val(parseInt(newCartQuantity) - parseInt('1'));
		
		
		ACC.track.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
			
	    form.submit();

	    return true;
	}
	
	
};

$(document).ready(function() {
    $('.js-cartItemDetailBtn').click(function(event) {
        event.stopPropagation();
        var thisDetailGroup =  $(this).parent('.js-cartItemDetailGroup');
        $(thisDetailGroup).toggleClass('open'); //only in its parent
        if ( $(thisDetailGroup).hasClass('open') )  {
            //close all if not this parent
            $('.js-cartItemDetailGroup').not( thisDetailGroup ).removeClass('open');
            //change aria
            $('.js-cartItemDetailBtn').attr('aria-expanded', 'true');

        } else {
            $('.js-cartItemDetailBtn').attr('aria-expanded', 'false');
        }
        $(document).click( function(){
            $(thisDetailGroup).removeClass('open');
        }); // closes when clicking outside this div
    });

    //enable comment for this item only
    $('.js-entry-comment-button').click(function(event) {
        event.preventDefault();
        var linkID = $(this).attr('href');
        $( linkID ).toggleClass('in');
        $( thisDetailGroup ).removeClass('open');
    });
});

