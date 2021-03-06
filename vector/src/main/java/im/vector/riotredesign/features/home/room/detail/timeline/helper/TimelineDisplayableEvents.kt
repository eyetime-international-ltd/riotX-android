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

package im.vector.riotredesign.features.home.room.detail.timeline.helper

import im.vector.matrix.android.api.session.events.model.EventType
import im.vector.matrix.android.api.session.room.timeline.TimelineEvent

object TimelineDisplayableEvents {

    val DISPLAYABLE_TYPES = listOf(
            EventType.MESSAGE,
            EventType.STATE_ROOM_NAME,
            EventType.STATE_ROOM_TOPIC,
            EventType.STATE_ROOM_MEMBER,
            EventType.STATE_HISTORY_VISIBILITY,
            EventType.CALL_INVITE,
            EventType.CALL_HANGUP,
            EventType.CALL_ANSWER,
            EventType.ENCRYPTED,
            EventType.ENCRYPTION,
            EventType.STATE_ROOM_THIRD_PARTY_INVITE,
            EventType.STICKER,
            EventType.STATE_ROOM_CREATE
    )
}

fun TimelineEvent.isDisplayable(): Boolean {
    return TimelineDisplayableEvents.DISPLAYABLE_TYPES.contains(root.type) && !root.content.isNullOrEmpty()
}

fun List<TimelineEvent>.filterDisplayableEvents(): List<TimelineEvent> {
    return this.filter {
        it.isDisplayable()
    }
}

fun List<TimelineEvent>.nextDisplayableEvent(index: Int): TimelineEvent? {
    return if (index == size - 1) {
        null
    } else {
        subList(index + 1, this.size).firstOrNull { it.isDisplayable() }
    }
}