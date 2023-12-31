var autoCompleteLocations = function() {
	"use strict";
	const t = (t, s) => {
			for (let i in s) "addClass" === i ? e(t, "add", s[i]) : "removeClass" === i ? e(t, "remove", s[i]) : t.setAttribute(i, s[i])
		},
		s = t => (t.firstElementChild || t).textContent.trim(),
		i = (t, s) => {
			t.scrollTop = t.offsetTop - s.offsetHeight
		},
		h = function(t, s) {
			void 0 === t && (t = !1), t && (e(t, "remove", "hidden"), r(t, "click", s))
		},
		e = (t, s, i) => t.classList[s](i),
		a = (s, i) => {
			t(s, {
				"aria-activedescendant": i || ""
			})
		},
		n = (t, s, i, h) => {
			const e = h.previousSibling,
				a = e ? e.offsetHeight : 0;
			if ("0" == t.getAttribute("aria-posinset") && (h.scrollTop = t.offsetTop - ((t, s) => {
					const i = document.querySelectorAll("#" + t + " > li:not(." + s + ")");
					let h = 0;
					return [].slice.call(i).map((t => h += t.offsetHeight)), h
				})(s, i)), t.offsetTop - a < h.scrollTop) h.scrollTop = t.offsetTop - a;
			else {
				const s = t.offsetTop + t.offsetHeight - a;
				s > h.scrollTop + h.offsetHeight && (h.scrollTop = s - h.offsetHeight)
			}
		},
		l = t => document.createElement(t),
		o = t => document.querySelector(t),
		r = (t, s, i) => {
			t.addEventListener(s, i)
		},
		c = (t, s, i) => {
			t.removeEventListener(s, i)
		},
		d = 27,
		u = 13,
		m = 38,
		p = 40,
		v = 9;
	return class {
		constructor(b, f) {
			let {
				delay: x = 500,
				clearButton: C = !0,
				clearButtonOnInitial: y = !1,
				howManyCharacters: k = 1,
				selectFirst: w = !1,
				insertToInput: g = !1,
				showAllValues: j = !1,
				cache: V = !1,
				disableCloseOnSelect: O = !1,
				classGroup: S,
				classPreventClosing: I,
				classPrefix: A,
				ariaLabelClear: R,
				onSearch: B,
				onResults: P = (() => {}),
				onSubmit: T = (() => {}),
				onOpened: G = (() => {}),
				onReset: J = (() => {}),
				onRender: N = (() => {}),
				onClose: $ = (() => {}),
				noResults: q = (() => {}),
				onSelectedItem: z = (() => {})
			} = f;
			var E;
			this.t = () => {
				var s, i, e, a, n;
				this.i(), s = this.h, i = this.l, e = this.o, a = this.u, n = this.m, t(i, {
					id: e,
					tabIndex: "0",
					role: "listbox"
				}), t(a, {
					addClass: n + "-results-wrapper"
				}), a.insertAdjacentElement("beforeend", i), s.parentNode.insertBefore(a, s.nextSibling), r(this.h, "input", this.p), this.v && r(this.h, "click", this.p), this.C({
					element: this.h,
					results: this.l
				}), this.k && h(this.g, this.destroy)
			}, this.j = (t, s) => {
				this.V && ("update" === t ? this.h.setAttribute(this.O, s.value) : "remove" === t ? this.h.removeAttribute(this.O) : this.h.value = this.h.getAttribute(this.O))
			}, this.p = t => {
				let {
					target: s,
					type: i
				} = t;
				if ("true" === this.h.getAttribute("aria-expanded") && "click" === i) return;
				const h = s.value.replace(this.S, "\\$&");
				this.j("update", s);
				const e = this.v ? 0 : this.I;
				clearTimeout(this.A), this.A = setTimeout((() => {
					this.R(h.trim())
				}), e)
			}, this.B = () => {
				var s;
				t(this.h, {
					"aria-owns": this.P + "-list",
					"aria-expanded": "false",
					"aria-autocomplete": "list",
					"aria-activedescendant": "",
					role: "combobox",
					removeClass: "auto-expanded"
				}), e(this.u, "remove", this.T), this.G(o("." + this.J)), (0 == (null == (s = this.N) ? void 0 : s.length) && !this.$ || this.v) && (this.l.textContent = ""), this.q = this.F ? 0 : -1, this.L()
			}, this.R = t => {
				this.M = t, this.D(!0), h(this.g, this.destroy), 0 == t.length && this.H && e(this.g, "add", "hidden"), this.K > t.length && !this.v ? this.D() : this.U({
					currentValue: t,
					element: this.h
				}).then((s => {
					const i = this.h.value.length,
						h = s.length;
					this.N = Array.isArray(s) ? s : JSON.parse(JSON.stringify(s)), this.D(), this.W(), 0 == h && 0 == i && e(this.g, "add", "hidden"), 0 == h && i ? (e(this.h, "remove", "auto-expanded"), this.B(), this.X({
						element: this.h,
						currentValue: t,
						template: this.Y
					}), this.Z()) : (h > 0 || (t => t && "object" == typeof t && t.constructor === Object)(s)) && (this.q = this.F ? 0 : -1, this.Y(), this.Z())
				})).catch((() => {
					this.D(), this.B()
				}))
			}, this.D = t => this.h.parentNode.classList[t ? "add" : "remove"](this._), this.W = () => e(this.h, "remove", this.tt), this.Z = () => {
				r(this.h, "keydown", this.st), r(this.h, "click", this.it), r(document, "click", this.ht), ["mousemove", "click"].map((t => {
					r(this.l, t, this.et)
				}))
			}, this.Y = s => {
				t(this.h, {
					"aria-expanded": "true",
					addClass: this.m + "-expanded"
				}), this.l.textContent = "";
				const h = 0 === this.N.length ? this.nt({
					currentValue: this.M,
					matches: 0,
					template: s
				}) : this.nt({
					currentValue: this.M,
					matches: this.N,
					classGroup: this.lt
				});
				this.l.insertAdjacentHTML("afterbegin", h), e(this.u, "add", this.T);
				const a = this.lt ? ":not(." + this.lt + ")" : "";
				this.ot = document.querySelectorAll("#" + this.o + " > li" + a), (s => {
					for (let i = 0; i < s.length; i++) t(s[i], {
						role: "option",
						tabindex: "-1",
						"aria-selected": "false",
						"aria-setsize": s.length,
						"aria-posinset": i
					})
				})(this.ot), this.rt({
					type: "results",
					element: this.h,
					results: this.l
				}), this.ct(), i(this.l, this.u)
			}, this.ht = t => {
				let {
					target: s
				} = t, i = null;
				(s.closest("ul") && this.dt || s.closest("." + this.ut)) && (i = !0), s.id === this.P || i || this.B()
			}, this.ct = () => {
				if (this.G(o("." + this.J)), !this.F) return;
				const {
					firstElementChild: s
				} = this.l, i = this.lt && this.N.length > 0 && this.F ? s.nextElementSibling : s;
				this.vt({
					index: this.q,
					element: this.h,
					object: this.N[this.q]
				}), t(i, {
					id: this.bt + "-0",
					addClass: this.J,
					"aria-selected": "true"
				}), a(this.h, this.bt + "-0")
			}, this.it = () => {
				this.l.textContent.length > 0 && !e(this.u, "contains", this.T) && (t(this.h, {
					"aria-expanded": "true",
					addClass: this.m + "-expanded"
				}), e(this.u, "add", this.T), i(this.l, this.u), this.ct(), this.rt({
					type: "showItems",
					element: this.h,
					results: this.l
				}))
			}, this.et = t => {
				t.preventDefault();
				const {
					target: s,
					type: i
				} = t, h = s.closest("li"), a = null == h ? void 0 : h.hasAttribute("role"), n = this.J, l = o("." + n);
				h && a && !s.closest("." + this.ut) && ("click" === i && this.ft(h), "mousemove" !== i || e(h, "contains", n) || (this.G(l), this.xt(h), this.q = this.Ct(h), this.vt({
					index: this.q,
					element: this.h,
					object: this.N[this.q]
				})))
			}, this.ft = t => {
				t && 0 !== this.N.length ? (this.H && e(this.g, "remove", "hidden"), this.h.value = s(t), this.yt({
					index: this.q,
					element: this.h,
					object: this.N[this.q],
					results: this.l
				}), this.dt || (this.G(t), this.B()), this.j("remove")) : !this.dt && this.B()
			}, this.Ct = t => Array.prototype.indexOf.call(this.ot, t), this.st = t => {
				const {
					keyCode: i
				} = t, h = e(this.u, "contains", this.T), n = this.N.length + 1;
				switch (this.kt = o("." + this.J), i) {
					case m:
					case p:
						if (t.preventDefault(), n <= 1 && this.F || !h) return;
						if (i === m ? (this.q < 0 && (this.q = n - 1), this.q -= 1) : (this.q += 1, this.q >= n && (this.q = 0)), this.G(this.kt), this.q >= 0 && this.q < n - 1) {
							const t = this.ot[this.q];
							this.$ && h && (this.h.value = s(t)), this.vt({
								index: this.q,
								element: this.h,
								object: this.N[this.q]
							}), this.xt(t)
						} else this.j(), a(this.h), this.vt({
							index: null,
							element: this.h,
							object: null
						});
						break;
					case u:
						t.preventDefault(), this.ft(this.kt);
						break;
					case v:
					case d:
						t.stopPropagation(), this.B()
				}
			}, this.xt = s => {
				const i = this.bt + "-" + this.Ct(s);
				t(s, {
					id: i,
					"aria-selected": "true",
					addClass: this.J
				}), a(this.h, i), n(s, this.o, this.lt, this.l)
			}, this.G = s => {
				s && t(s, {
					id: "",
					removeClass: this.J,
					"aria-selected": "false"
				})
			}, this.i = () => {
				this.H && (t(this.g, {
					class: this.m + "-clear hidden",
					type: "button",
					title: this.wt,
					"aria-label": this.wt
				}), this.h.insertAdjacentElement("afterend", this.g))
			}, this.destroy = () => {
				this.H && e(this.g, "add", "hidden"), this.h.value = "", this.h.focus(), this.l.textContent = "", this.B(), this.W(), this.gt(this.h), c(this.h, "keydown", this.st), c(this.h, "click", this.it), c(document, "click", this.ht)
			}, this.P = b, this.h = document.getElementById(b), this.U = (E = B, Boolean(E && "function" == typeof E.then) ? B : t => {
				let {
					currentValue: s,
					element: i
				} = t;
				return Promise.resolve(B({
					currentValue: s,
					element: i
				}))
			}), this.nt = P, this.C = N, this.yt = T, this.vt = z, this.rt = G, this.gt = J, this.X = q, this.L = $, this.I = x, this.K = k, this.H = C, this.k = y, this.F = w, this.$ = g, this.v = j, this.lt = S, this.ut = I, this.wt = R || "clear the search query", this.m = A ? A + "-auto" : "auto", this.dt = O, this.V = V, this.o = this.m + "-" + this.P + "-results", this.O = "data-cache-auto-" + this.P, this._ = this.m + "-is-loading", this.T = this.m + "-is-active", this.J = this.m + "-selected", this.bt = this.m + "-selected-option", this.tt = this.m + "-error", this.S = /[|\\{}()[\]^$+*?.]/g, this.A = null, this.u = l("div"), this.l = l("ul"), this.g = l("button"), this.t()
		}
	}
}();