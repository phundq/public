import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { timer } from 'rxjs';
import { takeWhile } from 'rxjs/operators';
import { ResFileBrowserGetFolderDto } from '../res/res-file-browser-get-folder';

@Component({
  selector: 'app-simple-menu',
  templateUrl: './simple-menu.component.html',
  styleUrls: ['./simple-menu.component.scss']
})
export class SimpleMenuComponent implements OnInit, AfterViewInit {
  @Input() hasSpecTarget: boolean = false;
  @Input() paddingLeftSize: number = 1;
  @Input() simpleMenuData: ResFileBrowserGetFolderDto[] = [];
  @Input() selectedItem: string = "";

  @Output() selectItem: EventEmitter<ResFileBrowserGetFolderDto> = new EventEmitter<ResFileBrowserGetFolderDto>();
  @Output() contextMenuConfig: EventEmitter<any> = new EventEmitter<any>();

  public level1Menu: any = undefined;
  constructor() { }

  ngOnInit() {
    if (this.paddingLeftSize > 5)
      this.paddingLeftSize = 5;
  }

  ngAfterViewInit() {
    if (!this.hasSpecTarget && this.paddingLeftSize == 1) {
      const timeInterval = timer(100);
      timeInterval.pipe(
        takeWhile(() => this.level1Menu == undefined)
      )

        .subscribe(() => {
          this.level1Menu = document.querySelectorAll(".child-level-1");
          if (this.level1Menu[0]) {
            this.level1Menu[0].click();
          }
        })
    }
  }

  handlerSelectItem(item: ResFileBrowserGetFolderDto) {
    this.selectedItem = item.path;
    this.selectItem.emit(item);
  }

  handlerContextMenuConfig(event) {
    this.contextMenuConfig.emit(event);
  }

  onRightClick(event, item: ResFileBrowserGetFolderDto) {
    this.selectItem.emit(item);
    this.contextMenuConfig.emit(event);
  }

}
