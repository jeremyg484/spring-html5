Array.prototype.remove = function(from, to) {
	if (typeof from != "number")
		return this.remove(this.indexOf(from));
	var rest = this.slice((to || from) + 1 || this.length);
	this.length = from < 0 ? this.length + from : from;
	return this.push.apply(this, rest);
};

jQuery(function($) {
	var el = document.getElementById("glow");
	var frameRate = 30;
	//var speed = 5; // average # of messages per second
	var width = el.width;
	var height = el.height;
	//var sketch = new Processing.Sketch(function(){});
	var p = new Processing(el);
	var commits = [];
	p.size(width, height);
	p.colorMode(3);
	p.noStroke();
	p.noLoop();
	var font = p.loadFont("helvetica");
	var fontSize = 12;
	p.textFont(font, fontSize);
	var components = [];
	var bottom = 300;

	function whiteText(msg, x, y) {
		p.textFont(font, fontSize);
		p.fill(0, 0, 0);
		p.rect(x, y - fontSize, font.width(msg) * fontSize, fontSize + 2);
		p.fill(0, 0, 255);
		p.text(msg, x, y);
	}

	var Axes = function Axes() {
		return {
			update : function() {
			},
			draw : function() {
				p.stroke(0, 0, 128);
				p.line(15.5, 0, 15.5, height);
				p.line(15, bottom + height / 10 + 0.5, width, bottom + height
						/ 10 + 0.5);
				for ( var y = 0; y < height; y += height / 10) {
					p.line(10, y + 0.5, 15, y + 0.5);
				}
				p.noStroke();
				whiteText("overall activity", 20, fontSize * 1.5);
				whiteText("module", width - font.width("module") * fontSize
						* 1.5, bottom + height / 10 + fontSize * 1.2);
			}
		}
	};
	components.push(new Axes());

	var Circle = function Circle(height, author) {
		var maxAge = 100;
		var letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.".split("");
		var x = 0;
		for ( var i = 0; i < 3; i++)
			x += letters.indexOf(author.charAt(i).toUpperCase())
					/ Math.pow(letters.length, i + 1);
		return {
			age : 0,
			x : x * width * 4 / 5 + width / 5,
			y : bottom - Math.sin(x * Math.PI) * Math.min(height, 90) * 3,
			dx : (Math.random() * width - width / 2) * 0.001,
			dy : (Math.random() * height - height / 2) * 0.001,
			hue : Math.floor(x * 348),
			update : function() {
				this.age++;
				this.x += this.dx;
				this.y += this.dy;
				if (this.age >= maxAge) {
					components.remove(this);
				}
			},
			draw : function() {
				var age = this.age;
				var hue = this.hue + age * 0.1;
				var saturation = age + 155;
				var brightness1 = 192 - age;
				var brightness2 = 128 - age;
				var alpha = 150 - 1.5 * age;
				var r = age * age / 50 + 2 * age + 30; // parabola
				p.fill(hue, saturation, brightness1, alpha);
				p.ellipse(this.x, this.y, r, r);
				p.fill(hue, saturation, brightness2, alpha);
				p.ellipse(this.x, this.y, r * 4 / 5, r * 4 / 5);
				p.fill(0, 0, 255, alpha);
				p.textFont(font, fontSize);
				p
						.text(author,
								this.x
										- Math.floor(font.width(author)
												* fontSize / 2), this.y);
			}
		};
	};

	function parseDate(isoDateString) {
		var d = isoDateString.split(/[: -]/);
		return new Date(Date.UTC(d[0], d[1] - 1, d[2], d[3], d[4], d[5]));
	}

	function formatDate(date) {
		var d = date.getDate();
		d = "" + (d < 10 ? "0" : "") + d + " ";
		d += "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".split(/ /)[date
				.getMonth()]
				+ " ";
		d += date.getFullYear();
		d = "Sun Mon Tue Wed Thu Fri Sat".split(/ /)[date.getDay()] + " " + d;
		return d;
	}

	function formatTime(date) {
		var h = date.getHours();
		var ampm = h >= 12 ? "PM" : "AM";
		var t = "" + (h == 0 ? 12 : (h > 12 ? h - 12 : h)) + ":";
		var m = date.getMinutes();
		t += "" + (m < 10 ? "0" : "") + m + " " + ampm;
		return t;
	}

	/*var Glow = function Glow(commits) {
	  var mindate = commits[0].date.getTime();
	  var maxdate = mindate + commits[commits.length - 1].date.getTime();
	  var frames = commits.length * frameRate / speed;
	  var datedelta = (maxdate - mindate) / frames;
	  var frame = 0;
	  return {
	    update: function () {
	      frame++;
	      var d = mindate + frame * datedelta;
	      var last = null;
	      while (commits.length > 0 && commits[0].date.getTime() <= d) {
	        var c = commits.shift();
	        if (!last || last.module != c.module || last.date.getTime() != c.date.getTime()) {
	      	  last = c;
	      	  console.debug("Pushing circle for " + c.module + " at " + c.date.getTime());
	      	  components.push(new Circle(components.length, c.module));
	        }
	      }
	      if (frame % 5 == 0)
	        $("#date").html(formatDate(new Date(d)) + "<br/>" + formatTime(new Date(d)));
	    },
	    draw: function () {
	      p.background(0, 15);
	    }
	  };
	};*/

	var Glow = function Glow() {
		return {
			update : function() {
				var c = commits.shift();
				if (c) {
					/*if (window.console) {
						console.debug("Pushing circle #"+c.order+" for " + c.module + " at "
								+ c.date.getTime());
					}*/
					components.push(new Circle(components.length, c.author));
					$("#date").html(
							formatDate(c.date) + "<br/>" + formatTime(c.date));
				}
			},
			draw : function() {
				p.background(0, 15);
				//p.background(0, 0, 0, 100);
			}
		};
	};

	var animate = function(frameRate, pause) {
		var runner = function() {
			for ( var i = 0; i < components.length; i++) {
				components[i].update();
			}
			for ( var i = 0; i < components.length; i++) {
				components[i].draw();
			}
		};
		var interval = window.setInterval(runner, 1000 / frameRate);
		$("#glow").toggle(function() {
			window.clearInterval(interval);
			p.textFont(font, 24);
			p.fill(0, 0, 255);
			p.text("Paused", width / 2 - font.width("Paused") * 12, 30);
			runner(); // run once more to show message
		}, function() {
			interval = window.setInterval(runner, 1000 / frameRate);
		});
	};
	
	var offline = false;
	if (window.applicationCache.status == 0) {
	  console.debug("window.applicationCache.status == LOADED FROM NETWORK");
	} else {
	  console.debug("window.applicationCache.status == LOADED FROM CACHE");
	}

	var worker = new Worker("commitProcessor.js");
	worker.addEventListener("message", function(event) {
		var message = event.data;
		if (message instanceof String || typeof message === "string") {
			console.debug(message);
		} else {
			commits.push( {
				author : message.username,
				date : new Date(message.timestamp * 1000),//parseDate(commit.commitdate), 
				type : message.commitType,
				module : message.moduleName
			});
		}
	}, false);
	worker.addEventListener("error", function(e) {
		console.error('WORKER ERROR: Line ' + e.lineno + ' in ' + e.filename, ': ' + e.message);
	}, false);
	
	components.push(new Glow());
	animate(frameRate, $("#glow"));
	worker.postMessage("start");
});
