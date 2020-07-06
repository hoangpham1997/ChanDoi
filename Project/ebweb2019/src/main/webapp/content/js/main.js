(function ($) {
    'use strict';
    $(function () {
        // collapes menu side bar
        $(document).on('click', '#sidebar eb-sidebar > ul.nav > li.nav-item > a.nav-link', function (e) {
            var $this = $(this);
            var $navLink = $("#sidebar ul.sub-menu > li.nav-item > a.nav-link[aria-expanded='true']");
            $this.siblings('div.collapse').addClass('show');
            $('#sidebar eb-sidebar > ul.nav > li.nav-item > a.nav-link').not($this).siblings('div.collapse').removeClass('show').addClass('hide');
            $('#sidebar eb-sidebar > ul.nav > li.nav-item > a.nav-link').not($this).attr('aria-expanded', 'false');
            $navLink.attr('aria-expanded', 'false');
        });
        // set active a sidebar when click
        $(document).on('click', '#sidebar ul.sub-menu > li.nav-item > a.nav-link', function (e) {
            var $this = $(this);
            $this.attr('aria-expanded', 'true');
            // $('#sidebar ul.sub-menu > li.nav-item > a.nav-link').not($this).attr('aria-expanded','false');
            $this.parent('.nav-item').siblings('.nav-item').children('.nav-link').attr('aria-expanded', 'false');
        });
        var body = $('body.sidebar-dark');
        // toogle sidebar button - edit by anmt
        $(document).on("click", "button.navbar-toggler[data-toggle='minimize']", function () {
            if ((body.hasClass('sidebar-toggle-display')) || (body.hasClass('sidebar-absolute'))) {
                body.toggleClass('sidebar-hidden');
            } else {
                body.toggleClass('sidebar-icon-only');
            }
        });
    });
})(jQuery);
