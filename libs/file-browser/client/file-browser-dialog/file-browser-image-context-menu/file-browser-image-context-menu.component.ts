import { ContextMenuAction } from './../file-browser.i';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
@Component({
  selector: 'app-file-browser-image-context-menu',
  templateUrl: './file-browser-image-context-menu.component.html',
  styleUrls: ['./file-browser-image-context-menu.component.scss'],
})
export class FileBrowserImageContextMenuComponent implements OnInit {

  @Input() hasSpecTarget: boolean = false;
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

  handlerClickPreView(e){
    e.stopPropagation();
    this.action.emit(ContextMenuAction.PREVIEW)
  }

  handlerClickChoose(e){
    e.stopPropagation();
    this.action.emit(ContextMenuAction.CHOOSE)
  }

}
