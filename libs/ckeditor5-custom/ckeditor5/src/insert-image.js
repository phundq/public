import Plugin from '@ckeditor/ckeditor5-core/src/plugin';
import Command from '@ckeditor/ckeditor5-core/src/command';
import imageIcon from '@ckeditor/ckeditor5-core/theme/icons/image.svg';
import ButtonView from '@ckeditor/ckeditor5-ui/src/button/buttonview';

export class InsertImage extends Plugin {
    init() {
        const editor = this.editor;

        editor.commands.add('insertImage', new InsertImageCommand(editor));

        editor.ui.componentFactory.add('insertImage', locale => {
            const view = new ButtonView(locale);

            view.set({
                label: 'Insert image',
                icon: imageIcon,
                tooltip: true,
                class: "btn-insert-image"
            });

            // Callback executed once the image is clicked.
            // view.on('execute', () => {
            //     const imageUrl = prompt('Image URL');

            //     editor.model.change(writer => {
            //         const imageElement = writer.createElement('image', {
            //             src: imageUrl
            //         });

            //         // Insert the image in the current selection location.
            //         editor.model.insertContent(imageElement, editor.model.document.selection);
            //     });
            // });

            view.on('execute', () => {
                editor.execute('insertImage');
            });

            return view;
        });
    }
}

export default class InsertImageCommand extends Command {

    constructor(editor) {
        super(editor);
        this.refresh();
    }

    refresh() {
        // Remove default document listener to lower its priority.
        this.stopListening(this.editor.model.document, 'change');

        // Lower this command listener priority to be sure that refresh() will be called after link & image refresh.
        this.listenTo(this.editor.model.document, 'change', () => { this.isEnabled = true }, { priority: 'low' });
    }

    execute() {
        this.refresh();
        this.openFileBrowser();
    }

    /**
     * override this function in your component to execute your action
     * @returns none
     */
    openFileBrowser() {
        // to do
        return;
    }
}
