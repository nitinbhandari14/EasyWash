window.onload = function() {
	
var dps = [[],[],[]];
var chart = new CanvasJS.Chart("chartContainer", {
	theme: "light2", // "light1", "dark1", "dark2"
	animationEnabled: true,
	title: {
		text: "Diamond Production: 2006 - 2016"
	},
	axisX: {
		valueFormatString: "YYYY"
	},
	axisY: {
		title: "Volume (in million carats)"
	},
	legend: {
		cursor: "pointer",
		itemclick: toggleDataSeries,
		verticalAlign: "top"
	},
	data: [{
		type: "area",
		name: "Russia",
		showInLegend: true,
		xValueType: "dateTime",
		xValueFormatString: "YYYY",
		yValueFormatString: "#,##0.0mn carats",
		dataPoints: dps[0]
	},{
		type: "area",
		name: "Congo",
		showInLegend: true,
		xValueType: "dateTime",
		xValueFormatString: "YYYY",
		yValueFormatString: "#,##0.0mn carats",
		dataPoints: dps[1]
	},{
		type: "area",
		name: "Australia",
		showInLegend: true,
		xValueType: "dateTime",
		xValueFormatString: "YYYY",
		yValueFormatString: "#,##0.0mn carats",
		dataPoints: dps[2]
	}]
});
 
var yValue;
var xValue;
 
<c:forEach items="${dataPointsList}" var="dataPoints" varStatus="loop">
	<c:forEach items="${dataPoints}" var="dataPoint">
		yValue = parseFloat("${dataPoint.y}");
		xValue = parseFloat("${dataPoint.x}");
		dps[parseInt("${loop.index}")].push({
			x : xValue,
			y : yValue,
		});
	</c:forEach>
</c:forEach>
 
chart.render();
 
function toggleDataSeries(e) {
	if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
		e.dataSeries.visible = false;
	} else {
		e.dataSeries.visible = true;
	}
	e.chart.render();
}
 
}