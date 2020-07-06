import { EventEmitter, Injectable } from '@angular/core';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';

@Injectable()
export class CheckService {
    checkEvent = new EventEmitter();
    treeItem: TreeviewItem;
    parentID: string;
    id: string;

    checked(treeItem) {
        this.treeItem = treeItem;
        this.checkEvent.emit();
    }

    onSelect(treeItem, onSelect) {
        this.treeItem = treeItem;
        this.checkEvent.emit();
    }

    moveArrowUp(parentID, id) {
        this.parentID = parentID;
        this.id = id;
        this.checkEvent.emit();
    }
}
