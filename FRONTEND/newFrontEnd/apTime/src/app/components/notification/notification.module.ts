import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationsComponent } from './notifications/notifications.component';
import { NotificationsServiceService } from './notifications-service.service';
import { CustomMaterialModule } from '../../modules/material.module';

@NgModule({
  declarations: [NotificationsComponent],
  imports: [CommonModule, CustomMaterialModule],
  exports: [NotificationsComponent]
})
export class NotificationModule {}
