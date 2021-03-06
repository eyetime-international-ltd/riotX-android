/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.internal.session.room

import com.zhuinden.monarchy.Monarchy
import im.vector.matrix.android.api.session.room.Room
import im.vector.matrix.android.internal.session.room.invite.InviteTask
import im.vector.matrix.android.internal.session.room.members.DefaultRoomMembersService
import im.vector.matrix.android.internal.session.room.members.LoadRoomMembersTask
import im.vector.matrix.android.internal.session.room.members.RoomMemberExtractor
import im.vector.matrix.android.internal.session.room.read.DefaultReadService
import im.vector.matrix.android.internal.session.room.read.SetReadMarkersTask
import im.vector.matrix.android.internal.session.room.send.DefaultSendService
import im.vector.matrix.android.internal.session.room.send.EventFactory
import im.vector.matrix.android.internal.session.room.state.DefaultStateService
import im.vector.matrix.android.internal.session.room.state.SendStateTask
import im.vector.matrix.android.internal.session.room.timeline.DefaultTimelineService
import im.vector.matrix.android.internal.session.room.timeline.GetContextOfEventTask
import im.vector.matrix.android.internal.session.room.timeline.PaginationTask
import im.vector.matrix.android.internal.session.room.timeline.TimelineEventFactory
import im.vector.matrix.android.internal.task.TaskExecutor

internal class RoomFactory(private val loadRoomMembersTask: LoadRoomMembersTask,
                           private val inviteTask: InviteTask,
                           private val sendStateTask: SendStateTask,
                           private val monarchy: Monarchy,
                           private val paginationTask: PaginationTask,
                           private val contextOfEventTask: GetContextOfEventTask,
                           private val setReadMarkersTask: SetReadMarkersTask,
                           private val eventFactory: EventFactory,
                           private val taskExecutor: TaskExecutor) {

    fun instantiate(roomId: String): Room {
        val roomMemberExtractor = RoomMemberExtractor(roomId)
        val timelineEventFactory = TimelineEventFactory(roomMemberExtractor)
        val timelineService = DefaultTimelineService(roomId, monarchy, taskExecutor, contextOfEventTask, timelineEventFactory, paginationTask)
        val sendService = DefaultSendService(roomId, eventFactory, monarchy)
        val stateService = DefaultStateService(roomId, sendStateTask, taskExecutor)
        val roomMembersService = DefaultRoomMembersService(roomId, monarchy, loadRoomMembersTask, inviteTask, taskExecutor)
        val readService = DefaultReadService(roomId, monarchy, setReadMarkersTask, taskExecutor)

        return DefaultRoom(
                roomId,
                monarchy,
                timelineService,
                sendService,
                stateService,
                readService,
                roomMembersService
        )
    }

}