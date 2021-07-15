import { Component, Inject, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
export interface FileBrowserFolderEditData {
  name: string;
  nameOrigin?: string;
};
@Component({
  selector: 'app-file-browser-folder-edit-dialog',
  templateUrl: './file-browser-folder-edit-dialog.component.html',
  styleUrls: ['./file-browser-folder-edit-dialog.component.scss'],
})
export class FileBrowserFolderEditDialogComponent implements OnInit {
  public dataEdit: FileBrowserFolderEditData = {
    name: ''
  };

  @ViewChild("folderName", {static: true}) folderName : ElementRef<HTMLInputElement>
  constructor(
    public dialogRef: MatDialogRef<FileBrowserFolderEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FileBrowserFolderEditData,
  ) { }

  ngOnInit() {
    if (this.data && this.data.nameOrigin)
      this.dataEdit = JSON.parse(JSON.stringify(this.data));
    this.folderName.nativeElement.focus();
  }

  handlerChangeName(name: string){
    this.dataEdit.name = name;
  }

  onNo() {
    this.dialogRef.close();
  }

  onYes() {
    this.dataEdit.name = this.dataEdit.name.trim();
    this.dialogRef.close(this.dataEdit);
  }
}
