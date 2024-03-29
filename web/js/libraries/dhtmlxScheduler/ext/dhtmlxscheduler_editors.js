/*
@license
dhtmlxScheduler v.5.0.0 Stardard

This software is covered by GPL license. You also can obtain Commercial or Enterprise license to use it in non-GPL project - please contact sales@dhtmlx.com. Usage without proper license is prohibited.

(c) Dinamenta, UAB.
*/
scheduler.form_blocks.combo = {
    render: function(e) {
        e.cached_options || (e.cached_options = {});
        var t = "";
        return t += "<div class='" + e.type + "' style='height:" + (e.height || 20) + "px;' ></div>"
    },
    set_value: function(e, t, a, r) {
        ! function() {
            function t() {
                if (e._combo && e._combo.DOMParent) {
                    var t = e._combo;
                    t.unload ? t.unload() : t.destructor && t.destructor(), t.DOMParent = t.DOMelem = null
                }
            }
            t();
            var a = scheduler.attachEvent("onAfterLightbox", function() {
                t(), scheduler.detachEvent(a)
            })
        }(), window.dhx_globalImgPath = r.image_path || "/", e._combo = new dhtmlXCombo(e, r.name, e.offsetWidth - 8),
        r.onchange && e._combo.attachEvent("onChange", r.onchange), r.options_height && e._combo.setOptionHeight(r.options_height);
        var i = e._combo;
        if (i.enableFilteringMode(r.filtering, r.script_path || null, !!r.cache), r.script_path) {
            var s = a[r.map_to];
            s ? r.cached_options[s] ? (i.addOption(s, r.cached_options[s]), i.disable(1), i.selectOption(0), i.disable(0)) : scheduler.$ajax.get(r.script_path + "?id=" + s + "&uid=" + scheduler.uid(), function(e) {
                var t, a = e.xmlDoc.responseText;
                try {
                    var n = JSON.parse(a);
                    t = n.options[0].text
                } catch (d) {
                    var l = scheduler.$ajax.xpath("//option", e.xmlDoc)[0];
                    t = l.childNodes[0].nodeValue
                }
                r.cached_options[s] = t, i.addOption(s, t), i.disable(1), i.selectOption(0), i.disable(0)
            }) : i.setComboValue("")
        } else {
            for (var n = [], d = 0; d < r.options.length; d++) {
                var l = r.options[d],
                    o = [l.key, l.label, l.css];
                n.push(o)
            }
            if (i.addOption(n), a[r.map_to]) {
                var h = i.getIndexByValue(a[r.map_to]);
                i.selectOption(h)
            }
        }
    },
    get_value: function(e, t, a) {
        var r = e._combo.getSelectedValue();
        return a.script_path && (a.cached_options[r] = e._combo.getSelectedText()), r
    },
    focus: function(e) {}
}, scheduler.form_blocks.radio = {
    render: function(e) {
        var t = "";
        t += "<div class='dhx_cal_ltext dhx_cal_radio' style='height:" + e.height + "px;' >";
        for (var a = 0; a < e.options.length; a++) {
            var r = scheduler.uid();
            t += "<input id='" + r + "' type='radio' name='" + e.name + "' value='" + e.options[a].key + "'><label for='" + r + "'> " + e.options[a].label + "</label>", e.vertical && (t += "<br/>")
        }
        return t += "</div>"
    },
    set_value: function(e, t, a, r) {
        for (var i = e.getElementsByTagName("input"), s = 0; s < i.length; s++) {
            i[s].checked = !1;
            var n = a[r.map_to] || t;
            i[s].value == n && (i[s].checked = !0);
        }
    },
    get_value: function(e, t, a) {
        for (var r = e.getElementsByTagName("input"), i = 0; i < r.length; i++)
            if (r[i].checked) return r[i].value
    },
    focus: function(e) {}
}, scheduler.form_blocks.checkbox = {
    render: function(e) {
        return scheduler.config.wide_form ? '<div class="dhx_cal_wide_checkbox" ' + (e.height ? "style='height:" + e.height + "px;'" : "") + "></div>" : ""
    },
    set_value: function(e, t, a, r) {
        e = document.getElementById(r.id);
        var i = scheduler.uid(),
            s = "undefined" != typeof r.checked_value ? t == r.checked_value : !!t;
        e.className += " dhx_cal_checkbox";
        var n = "<input id='" + i + "' type='checkbox' value='true' name='" + r.name + "'" + (s ? "checked='true'" : "") + "'>",
            d = "<label for='" + i + "'>" + (scheduler.locale.labels["section_" + r.name] || r.name) + "</label>";
        if (scheduler.config.wide_form ? (e.innerHTML = d, e.nextSibling.innerHTML = n) : e.innerHTML = n + d, r.handler) {
            var l = e.getElementsByTagName("input")[0];
            l.onclick = r.handler
        }
    },
    get_value: function(e, t, a) {
        e = document.getElementById(a.id);
        var r = e.getElementsByTagName("input")[0];
        return r || (r = e.nextSibling.getElementsByTagName("input")[0]),
            r.checked ? a.checked_value || !0 : a.unchecked_value || !1
    },
    focus: function(e) {}
};
//# sourceMappingURL=../sources/ext/dhtmlxscheduler_editors.js.map