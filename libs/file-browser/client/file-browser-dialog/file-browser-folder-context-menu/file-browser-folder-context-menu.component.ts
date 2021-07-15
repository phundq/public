import { ContextMenuAction } from './../file-browser.i';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
@Component({
  selector: 'app-file-browser-folder-context-menu',
  templateUrl: './file-browser-folder-context-menu.component.html',
  styleUrls: ['./file-browser-folder-context-menu.component.scss'],
})
export class FileBrowserFolderContextMenuComponent implements OnInit {

  @Input() x: number = 0;
  @Input() y: number = 0;
  @Output() action: EventEmitter<ContextMenuAction> = new EventEmitter<ContextMenuAction>();
  constructor() { }

  ngOnInit() {

  }

  handlerClickDelete(e){
    e.stopPropagation();
    this.action.emit(ContextMenuAction.DELETE);
  }
  handlerClickMove(e){
    e.stopPropagation();
    this.action.emit(ContextMenuAction.MOVE)
  }
  handlerClickRename(e){
    e.stopPropagation();
    this.action.emit(ContextMenuAction.RENAME)
  }

}
