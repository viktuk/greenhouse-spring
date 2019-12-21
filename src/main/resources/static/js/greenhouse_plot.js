var  unitSelector = document.getElementById("unitSelector");

unitSelector.addEventListener('change', function() {
    refreshPlot(this.value);
});

var i0 = d3.interpolateHsvLong(d3.hsv(120, 1, 0.65), d3.hsv(60, 1, 0.90)),
    i1 = d3.interpolateHsvLong(d3.hsv(60, 1, 0.90), d3.hsv(0, 0, 0.95)),
    interpolateTerrain = function (t) {
        return t < 0.5 ? i0(t * 2) : i1((t - 0.5) * 2);
    },
    color = d3.scaleSequential(interpolateTerrain).domain([90, 190]);

function refreshPlot(unit) {
    console.log("refreshing plot " + unit);
    d3.json("./unit/" + unit, function (error, environment) {
        if (error) throw error;

        var n = environment.maxX,
            m = environment.maxY;

        var canvas = d3.select("canvas")
            .attr("width", n)
            .attr("height", m);

        var context = canvas.node().getContext("2d"),
            image = context.createImageData(n, m);

        for (var j = 0, l = 0; j < m; ++j) {
            for (var i = 0; i < n; ++i, l += 4) {
                var c = d3.rgb(color(environment.matrix[i][j]));
                image.data[l] = c.r;
                image.data[l + 1] = c.g;
                image.data[l + 2] = c.b;
                image.data[l + 3] = 255;
            }
        }

        context.putImageData(image, 0, 0);
    });
}

function refresh() {
    refreshPlot(unitSelector.options[unitSelector.selectedIndex].value);
}

var interval;
var started = false;

function startAutoRefresh() {
    if(started){
        return;
    }
    interval = setInterval(refresh, 1000);
    started = true;
}

function stopAutoRefresh() {
    clearInterval(interval);
    started = false;
}

refresh();

