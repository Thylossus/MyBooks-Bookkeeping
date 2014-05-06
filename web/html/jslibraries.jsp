<%-- 
    Document   : jslibraries
    Created on : 29.04.2014, 10:35:14
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<!-- Bootstrap core JavaScript -->
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/jquery.min.js"></script>
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $(".nav li.disabled a").click(function(e) {
            e.preventDefault();
            return false;
        });
    });
</script>