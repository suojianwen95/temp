var getModule = function()
{
    return document.getElementById("module").value;
};
CKEDITOR.editorConfig = function( config ) {
    config.language = 'zh-cn';
    config.toolbarGroups = [
        { name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
        { name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
        { name: 'links' },
        { name: 'insert' },
        { name: 'forms' },
        { name: 'tools' },
        { name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
        { name: 'others' },
        '/',
        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
        { name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
        { name: 'styles' },
        { name: 'colors' },
        { name: 'about' }
    ];
    // Remove some buttons provided by the standard plugins, which are
    // not needed in the Standard(s) toolbar.
    //config.removeButtons = 'Underline,Subscript,Superscript';
    //config.skin='minimalist',
    // Set the most common block elements.
    config.format_tags = 'p;h1;h2;h3;pre';
    config.startupOutlineBlocks = false;
    // Simplify the dialog windows.
    config.removeDialogTabs = 'image:advanced;link:advanced';

    config.image_previewText = ' ';
    config.filebrowserUploadUrl = "/dash/upload";
    //config.contentsCss = ['/assets/dist/css/modules/house/house-type/article-style.min.css'];
    config.allowedContent = "h1 h2 h3 p blockquote strong em del ins table tr th td caption;" +
        "a[!href,target];" +
        "img(left,right)[!src,alt,width,height];" +
        "span{!font-family};span{!color};span(!marker);";
};
CKEDITOR.on('dialogDefinition', function(ev)
{
    var dialogName = ev.data.name;
    var dialogDefinition = ev.data.definition;
     if ( dialogName == 'link' )
    {
        // Get a reference to the 'Target' tab.
        var targetTab = dialogDefinition.getContents( 'target' );
        // Get a reference to the target select field.
        var targetField = targetTab.get('linkTargetType');
        // Remove unnecessary target options.
        targetField.items.splice(2,1);

        var targetTabUpload = dialogDefinition.getContents( 'upload' );
        
        for (var i = 0; i < targetTabUpload.elements.length; i++) {
            var el = targetTabUpload.elements[i];

            if (el.type !== 'fileButton') {
                continue;
            }

            // add onClick for submit button to add inputs or rewrite the URL
            var onClick = el.onclick;

            el.onClick = function(evt) {
                var dialog = this.getDialog();
                var fb = dialog.getContentElement(this['for'][0], this['for'][1]);
                var action = fb.getAction();
                var editor = dialog.getParentEditor();
                editor._.filebrowserSe = this;

                // if using jQuery
                $(fb.getInputElement().getParent().$).append('<input type="hidden" name="module" value="'+ getModule() +'">');

                // modifying the URL


                if (onClick && onClick.call(evt.sender, evt) === false) {
                        return false;
                }

                return true;
            };
        }

    } else if (dialogName == 'image') {
        var uploadTab = dialogDefinition.getContents( 'Upload' );
        
            for (var i = 0; i < uploadTab.elements.length; i++) {
            var el = uploadTab.elements[i];

            if (el.type !== 'fileButton') {
                continue;
            }

            // add onClick for submit button to add inputs or rewrite the URL
            var onClick = el.onclick;

            el.onClick = function(evt) {
                var dialog = this.getDialog();
                var fb = dialog.getContentElement(this['for'][0], this['for'][1]);
                var action = fb.getAction();
                var editor = dialog.getParentEditor();
                editor._.filebrowserSe = this;

                // if using jQuery
                $(fb.getInputElement().getParent().$).append('<input type="hidden" name="module" value="'+ getModule() +'">');

                // modifying the URL


                if (onClick && onClick.call(evt.sender, evt) === false) {
                        return false;
                }

                return true;
            };
        }
    }
});