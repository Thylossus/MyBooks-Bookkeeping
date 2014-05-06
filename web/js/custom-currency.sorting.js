jQuery.fn.dataTableExt.aTypes.push(
    function ( sData )
    {
        var sValidChars = "0123456789.,-";
        var Char;
         
        /* Check the numeric part */
        for ( i=0 ; i<sData.length ; i++ )
        {
            Char = sData.charAt(i);
            if (sValidChars.indexOf(Char) === -1)
            {
                return null;
            }
        }
         console.log("called");
        /* Check suffix by currency */
        if (sData.charAt(sData.length-1) === ' ' && sData.charAt(sData.length-1) === '£' )
        {
            return 'currency';
        }
        return null;
    }
);

jQuery.extend( jQuery.fn.dataTableExt.oSort, {
    "currency-pre": function ( a ) {
        a = a.substring(0, a.length-2);
        console.log("jey");
        return parseFloat( a );
    },
 
    "currency-asc": function ( a, b ) {
        return a - b;
    },
 
    "currency-desc": function ( a, b ) {
        return b - a;
    }
} );