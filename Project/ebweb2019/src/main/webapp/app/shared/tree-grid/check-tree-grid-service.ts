import { EventEmitter, Injectable } from '@angular/core';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { TreeAccountListItem } from 'app/shared/tree-grid/tree-item';
import { start } from 'repl';

@Injectable()
export class CheckTreeGridService {
    checkEvent = new EventEmitter();
    treeItemGrid: TreeAccountListItem;
    startTreeItemGrid: TreeAccountListItem;
    selectedRows: TreeAccountListItem[];
    endTreeItemGrid: TreeAccountListItem;
    startItem: TreeAccountListItem;
    parentID: string;
    isMultiRow: boolean;

    multiSelect(startTreeItemGrid, endTreeItemGrid) {
        this.startTreeItemGrid = startTreeItemGrid;
        this.endTreeItemGrid = endTreeItemGrid;
        this.checkEvent.emit();
    }

    checked(treeItemGrid) {
        this.treeItemGrid = treeItemGrid;
        this.checkEvent.emit();
    }

    moveArrowUp(id) {
        this.parentID = id;
        this.checkEvent.emit();
    }
}
