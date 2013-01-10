function ListViewModel(){
    var _this = this;

    _this.layouts = ko.observableArray();
    _this.selectedItem = ko.observable(null);

    _this.name = ko.observable();
    _this.text = ko.observable();

    _this.fillList = function(){
        _this.layouts([]);
        $.get('/jersey-webapp/webresources/layouts/list', function (data) {
            $.each(data, function(index, item){
                _this.layouts.push(item);
            });
        });
    };

    _this.selectItem = function(item){
        _this.selectedItem(item);
        _this.name(item.name);
        _this.text(item.text);
    };

    _this.cancelEdit = function(){
        _this.fillList();
        _this.selectedItem(null);
    };

    _this.saveItem = function(){
        var item = _this.selectedItem();
        if(item != null){
            item.name = _this.name();
            item.text = _this.text();
            $.post('/jersey-webapp/webresources/layouts/', item);
            $.ajax({
                contentType: 'application/json',
                data: JSON.stringify(item),
                dataType: 'json',
                success: function () {
                    _this.cancelEdit();
                },
                error: function () {
                    alert("Update Failed");
                },
                processData: false,
                type: 'POST',
                url: '/jersey-webapp/webresources/layouts/'
            });
        }
    };

    _this.deleteItem = function(){
        var item = _this.selectedItem();
        if(item != null){
            item.name = _this.name();
            item.text = _this.text();
            $.ajax({
                success: function (data) {
                    _this.layouts.remove(item);
                    _this.selectedItem(null);
                },
                type: 'DELETE',
                url: '/jersey-webapp/webresources/layouts/?id=' + item.id
            });
        }
    };
}

function RowViewModel(table){
    var _this = this;

    _this.table = table;
    _this.items = ko.observableArray();

    _this.addItem = function(){
        var item = new ItemViewModel(_this);
        item.text("new Item");
        _this.items.push(item);
    };

    _this.removeRow = function(){
        $.each(_this.items(), function(index, item){
            if(item == _this.table.selectedItem()){
                _this.table.selectedItem(null);
            }
        });

        _this.table.rows.remove(_this);
    };
}

function ItemViewModel(row){
    var _this = this;

    _this.row = row;

    _this.specKey = ko.observable(false);
    _this.text = ko.observable();
    _this.charValue = ko.observable();
    _this.specValue = ko.observable();
}

function specKeyOption(name, value){
    var _this = this;

    _this.name = name;
    _this.value = value;
}

function NewLayoutViewModel(){
    var _this = this;

    var specKeyCodes = ["VK_LBUTTON","VK_RBUTTON","VK_CANCEL","VK_MBUTTON","VK_BACK","VK_TAB","VK_CLEAR","VK_RETURN","VK_SHIFT","VK_CONTROL","VK_MENU","VK_PAUSE","VK_CAPITAL","VK_KANA","VK_HANGUL","VK_JUNJA","VK_FINAL","VK_HANJA","VK_KANJI","VK_ESCAPE","VK_CONVERT","VK_NONCONVERT","VK_ACCEPT","VK_MODECHANGE","VK_SPACE","VK_PRIOR","VK_NEXT","VK_END","VK_HOME","VK_LEFT","VK_UP","VK_RIGHT","VK_DOWN","VK_SELECT","VK_PRINT","VK_EXECUTE","VK_SNAPSHOT","VK_INSERT","VK_DELETE","VK_HELP","VK_LWIN","VK_RWIN","VK_APPS","VK_SLEEP","VK_NUMPAD0","VK_NUMPAD1","VK_NUMPAD2","VK_NUMPAD3","VK_NUMPAD4","VK_NUMPAD5","VK_NUMPAD6","VK_NUMPAD7","VK_NUMPAD8","VK_NUMPAD9","VK_MULTIPLY","VK_ADD","VK_SEPARATOR","VK_SUBTRACT","VK_DECIMAL","VK_DIVIDE","VK_F1","VK_F2","VK_F3","VK_F4","VK_F5","VK_F6","VK_F7","VK_F8","VK_F9","VK_F10","VK_F11","VK_F12","VK_F13","VK_F14","VK_F15","VK_F16","VK_F17","VK_F18","VK_F19","VK_F20","VK_F21","VK_F22","VK_F23","VK_F24","VK_NUMLOCK","VK_SCROLL","VK_OEM_NEC_EQUAL","VK_OEM_FJ_JISHO","VK_OEM_FJ_MASSHOU","VK_OEM_FJ_TOUROKU","VK_OEM_FJ_LOYA","VK_OEM_FJ_ROYA","VK_LSHIFT","VK_RSHIFT","VK_LCONTROL","VK_RCONTROL","VK_LMENU","VK_RMENU","VK_BROWSER_BACK","VK_BROWSER_FORWARD","VK_BROWSER_REFRESH","VK_BROWSER_STOP","VK_BROWSER_SEARCH","VK_BROWSER_FAVORITES","VK_BROWSER_HOME","VK_VOLUME_MUTE","VK_VOLUME_DOWN","VK_VOLUME_UP","VK_MEDIA_NEXT_TRACK","VK_MEDIA_PREV_TRACK","VK_MEDIA_STOP","VK_MEDIA_PLAY_PAUSE","VK_LAUNCH_MAIL","VK_LAUNCH_MEDIA_SELECT","VK_LAUNCH_APP1","VK_LAUNCH_APP2","VK_OEM_1","VK_OEM_PLUS","VK_OEM_COMMA","VK_OEM_MINUS","VK_OEM_PERIOD","VK_OEM_2","VK_OEM_3","VK_OEM_4","VK_OEM_5","VK_OEM_6","VK_OEM_7","VK_OEM_8","VK_OEM_AX","VK_OEM_102","VK_ICO_HELP","VK_ICO_00","VK_PROCESSKEY","VK_ICO_CLEAR","VK_PACKET","VK_OEM_RESET","VK_OEM_JUMP","VK_OEM_PA1","VK_OEM_PA2","VK_OEM_PA3","VK_OEM_WSCTRL","VK_OEM_CUSEL","VK_OEM_ATTN","VK_OEM_FINISH","VK_OEM_COPY","VK_OEM_AUTO","VK_OEM_ENLW","VK_OEM_BACKTAB","VK_ATTN","VK_CRSEL","VK_EXSEL","VK_EREOF","VK_PLAY","VK_ZOOM","VK_NONAME","VK_PA1","VK_OEM_CLEAR"];

    _this.name = ko.observable();
    _this.selectedItem = ko.observable();
    _this.rows = ko.observableArray();
    _this.specKeys = ko.observableArray(
        $.map(specKeyCodes, function(item){
            var name = item.substr(3).replace("_", " ");
            return new specKeyOption(name, item);
        })
    );

    _this.addRow = function(){
        _this.rows.push(new RowViewModel(_this));
    }

    _this.selectItem = function(item){
        _this.selectedItem(item);
    }

    _this.removeSelectedItem = function(){
        var item = _this.selectedItem();
        item.row.items.remove(item);
        _this.selectedItem(null);
    }

    _this.save = function(){
            var rootTag = $('<rc />');
            var tableTag = $('<table />');
            rootTag.append(tableTag);
            $.each(_this.rows(), function (index, row) {
                var rowTag = $('<row />');
                $.each(row.items(), function (index2, item) {
                    var buttonTag = $('<button />');
                    buttonTag.attr("text", item.text());
                    buttonTag.attr("key", item.specKey() ? item.specValue().value : item.charValue());
                    rowTag.append(buttonTag);
                });
                tableTag.append(rowTag);
            });

            var data = {
                'id' : null,
                'name' : _this.name(),
                'text' : $('<div />').append(rootTag).html()
            }

            $.ajax({
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: 'json',
                success: function (data2) {
                    location.hash = "#list";
                },
                error: function () {
                    alert("Update Failed");
                },
                processData: false,
                type: 'PUT',
                url: location.origin + '/jersey-webapp/webresources/layouts/'
            });
    };
}

function PageViewModel(){
    var _this = this;

    _this.currentPage = ko.observable();

    _this.listViewModel = new ListViewModel();
    _this.newLayoutViewModel = new NewLayoutViewModel();

    _this.changePage = function(page){
        if(page == 'list'){
            _this.listViewModel.fillList();
        }

        _this.currentPage(page);
    }

    // Client-side routes
    Sammy(function() {
        this.get('#:page', function() {
            var page = this.params.page;
            _this.changePage(page)
        });

        this.get('/', function() {
            this.app.runRoute('get', '#list')
        });
    }).run();
}

var viewModel = new PageViewModel();
ko.applyBindings(viewModel);

