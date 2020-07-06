import { Component, OnInit, Input } from '@angular/core';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-context-menu',
    templateUrl: './context-menu.component.html',
    styleUrls: ['./context-menu.component.css']
})
export class EbContextMenuComponent implements OnInit {
    @Input() contextMenu: ContextMenu;
    @Input() eventHandlingHere: boolean;
    @Input() useCopyRow: boolean;
    @Input() isMaterialGoodsSpecification: boolean;
    @Input() isMaterialGoodsSpecificationSelect: boolean;

    constructor(private eventManager: JhiEventManager) {}

    ngOnInit() {
        this.contextMenu = this.contextMenu ? this.contextMenu : {};
    }

    addNewRow() {
        if (!this.eventHandlingHere) {
            this.contextMenu.data.push({});
        }
        this.contextMenu.isShow = false;
        // sau khi them 1 dong
        this.eventManager.broadcast({
            name: 'afterAddNewRow',
            content: this.contextMenu.selectedData
        });
    }

    deleteRow() {
        if (!this.eventHandlingHere) {
            this.contextMenu.data.splice(this.contextMenu.data.indexOf(this.contextMenu.selectedData), 1);
        }
        this.contextMenu.isShow = false;
        // sau khi xoa 1 dong
        this.eventManager.broadcast({
            name: 'afterDeleteRow',
            content: this.contextMenu.selectedData
        });
    }

    copyRow() {
        if (!this.eventHandlingHere) {
            const copy = Object.assign({}, this.contextMenu.selectedData ? this.contextMenu.selectedData : {});
            copy.id = null;
            copy.orderPriority = null;
            this.contextMenu.data.push(copy);
        }
        this.contextMenu.isShow = false;
        // sau khi them 1 dong
        this.eventManager.broadcast({
            name: 'afterCopyRow',
            content: this.contextMenu.selectedData
        });
    }

    selectMaterialGoodsSpecification() {
        this.eventManager.broadcast({
            name: 'selectMaterialGoodsSpecification',
            content: this.contextMenu.selectedData
        });
    }

    getYOffset() {
        this.contextMenu.y = this.contextMenu && this.contextMenu.event ? this.contextMenu.event.pageY : 0;
        return this.contextMenu.y;
    }

    getXOffset() {
        if (this.contextMenu && this.contextMenu.event) {
            if (this.contextMenu.event.pageX > 1290) {
                return this.contextMenu.event.pageX - 200;
            } else if (this.contextMenu.event.pageX < 1290) {
                return this.contextMenu.event.pageX;
            }
        }
        return 0;
    }
}
