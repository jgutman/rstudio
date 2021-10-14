/*
 * SessionSuspend.hpp
 *
 * Copyright (C) 2021 by RStudio, PBC
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */

#ifndef SESSION_SUSPEND_HPP
#define SESSION_SUSPEND_HPP

#include <boost/function.hpp>

namespace rstudio {
namespace session {
namespace suspend {

enum SuspendBlockingOps
{
   kChildProcess = 0,
   kConnection,
   kOverlay,
   kExternalPointer,
   kActiveJob,
   kCommandPrompt,


   kBlockingOpsCount // Always keep this at bottom of enum list
};

bool disallowSuspend();
void addBlockingOp(SuspendBlockingOps op);
void removeBlockingOp(SuspendBlockingOps op);
void checkBlockingOp(bool blocking, SuspendBlockingOps op);
bool suspendSession(bool force, int status = EXIT_SUCCESS);
void suspendIfRequested(const boost::function<bool()>& allowSuspend);
void handleUSR1(int unused);
void handleUSR2(int unused);
bool suspendedFromTimeout();
void setSuspendedFromTimeout(bool suspended);
bool sessionResumed();
void setSessionResumed(bool resumed);

} // namespace suspend
} // namespace session
} // namespace rstudio

#endif /* SESSION_SUSPEND_HPP */
