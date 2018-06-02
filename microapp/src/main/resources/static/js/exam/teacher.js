type = ['', 'info', 'success', 'warning', 'danger'];


demo = {
    initPickColor: function () {
        $('.pick-class-label').click(function () {
            var new_class = $(this).attr('new-class');
            var old_class = $('#display-buttons').attr('data-class');
            var display_div = $('#display-buttons');
            if (display_div.length) {
                var display_buttons = display_div.find('.btn');
                display_buttons.removeClass(old_class);
                display_buttons.addClass(new_class);
                display_div.attr('data-class', new_class);
            }
        });
    },

    initChartist: function () {
        var data = {
            labels: ['12-01', '12-02', '12-03', '12-04', '12-05', '12-06', '12-07'],
            series: [
                [10, 15, 15, 20, 21, 23, 35],
                [1, 4, 2, 5, 3, 6, 7],
                [1, 10, 15, 19, 30, 35, 45],
                [1, 2, 3, 6, 5, 9, 9]
            ]
        };

        var options = {
            seriesBarDistance: 10,
            axisX: {
                showGrid: false
            },
            height: "245px"
        };

        var responsiveOptions = [
            ['screen and (max-width: 640px)', {
                seriesBarDistance: 5,
                axisX: {
                    labelInterpolationFnc: function (value) {
                        return value[0];
                    }
                }
            }]
        ];

        Chartist.Line('#chartActivity', data, options, responsiveOptions);

    }

}
