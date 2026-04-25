(function ($) {
    'use strict';

    const labelClasses = 'text-bg-warning text-bg-info text-bg-success';

    function ratingClass(percent) {
        if (percent < 30) {
            return 'text-bg-warning';
        }
        if (percent < 70) {
            return 'text-bg-info';
        }
        return 'text-bg-success';
    }

    function render($widget, value) {
        const rating = Math.max(0, Math.min(5, value || 0));
        const percent = rating * 20;

        $widget.find('.rating-star').each(function () {
            const $star = $(this);
            const starValue = Number($star.data('value'));
            const $icon = $star.find('.rating-star-icon');
            $icon.text(starValue <= rating ? '★' : '☆');
        });

        $widget.find('.rating-percent')
            .text(percent + '%')
            .removeClass(labelClasses)
            .addClass(ratingClass(percent));
    }

    function trackRating(packageName) {
        if (typeof window.gtag !== 'function') {
            return;
        }

        window.gtag('event', 'add_rating', {
            event_category: 'Rating',
            event_label: packageName
        });
    }

    $(function () {
        $('.package-rating').each(function () {
            const $widget = $(this);
            const average = Number($widget.data('average')) || 0;

            render($widget, average);

            $widget.on('mouseenter', '.rating-star', function () {
                render($widget, Number($(this).data('value')));
            });

            $widget.on('mouseleave', function () {
                render($widget, Number($widget.data('average')) || 0);
            });

            $widget.on('click', '.rating-star', function () {
                const score = Number($(this).data('value'));
                const packageName = String($widget.data('package-name'));
                const encodedPackageName = encodeURIComponent(packageName);
                const $message = $widget.find('.rating-message');

                $.ajax({
                    url: '/api/package/name/' + encodedPackageName + '/rating',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        packageName: packageName,
                        average: score
                    })
                }).done(function () {
                    $widget.data('average', score);
                    render($widget, score);
                    $message.text('Rating saved.').removeClass('d-none text-danger').addClass('text-success');
                    trackRating(packageName);
                }).fail(function () {
                    render($widget, Number($widget.data('average')) || 0);
                    $message.text('Unable to save rating. Please try again later.')
                        .removeClass('d-none text-success')
                        .addClass('text-danger');
                });
            });
        });
    });
}(jQuery));
