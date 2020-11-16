package com.websocket.board.service;

import com.websocket.board.model.Channel;
import com.websocket.board.model.user.User;
import com.websocket.board.model.user.UserChannel;
import com.websocket.board.payload.CreateChannelRequest;
import com.websocket.board.payload.InviteChannelRequest;
import com.websocket.board.payload.ValidUserWithChannelRequest;
import com.websocket.board.payload.WithdrawalRequest;
import com.websocket.board.repo.ChannelRepository;
import com.websocket.board.repo.UserChannelRepository;
import com.websocket.board.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserChannelRepository userChannelRepository;

    @Override
    public Boolean validUserWithChannel(ValidUserWithChannelRequest validUserWithChannelRequest) {
        String email = validUserWithChannelRequest.getEmail();
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) {
            return false;
        }

        String channelId = validUserWithChannelRequest.getChannelId();
        Optional<Channel> channel = channelRepository.findByChannelId(channelId);
        if(!channel.isPresent()) {
            return false;
        }

        Optional<UserChannel> userChannel = userChannelRepository.findByUserAndChannel(user.get(), channel.get());
        if(!userChannel.isPresent()) {
            return false;
        }

        return true;
    }

    @Override
    public Channel createChannel(CreateChannelRequest createChannelRequest) {
        Channel channel = new Channel().createChannel();
        channel.setChannelName(createChannelRequest.getChannelName());

        Channel channelResponse = channelRepository.save(channel);

        // Channel - User Mapping
        Optional<User> user = userRepository.findByEmail(createChannelRequest.getEmail());

        if(user.isPresent()) {
            UserChannel userChannel = new UserChannel().builder()
                    .user(user.get())
                    .channel(channelResponse)
                    .build();

            userChannelRepository.save(userChannel);
        }

        return channelResponse;
    }

    @Override
    public Boolean saveInvitedChannel(InviteChannelRequest inviteChannelRequest, String channelId) {
        Optional<Channel> channelResponse = channelRepository.findByChannelId(channelId);
        Optional<User> user = userRepository.findByEmail(inviteChannelRequest.getUser().getEmail());

        Boolean isSaved = false;
        if(user.isPresent()) {
            UserChannel userChannel = new UserChannel().builder()
                    .user(user.get())
                    .channel(channelResponse.get())
                    .build();

            userChannelRepository.save(userChannel);
            isSaved = true;
        }

        return isSaved;
    }

    @Override
    public List<Channel> getMyChannels(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        List<Channel> channels = new ArrayList<>();
        if(user.isPresent()) {
            Optional<List<UserChannel>> userChannels = userChannelRepository.findAllByUser(user.get());
            if(userChannels.isPresent()) {
                for (UserChannel userChannel: userChannels.get()) {
                    channels.add(userChannel.getChannel());
                }
            }
        }

        return channels;
    }

    @Override
    @Transactional
    public Boolean withdrawalChannel(WithdrawalRequest withdrawalRequest) {
        Optional<User> user = userRepository.findByEmail(withdrawalRequest.getEmail());
        Optional<Channel> channel = channelRepository.findByChannelId(withdrawalRequest.getChannelId());
        boolean isDeleted = false;

        if(user.isPresent()) {
            Optional<UserChannel> userChannel = userChannelRepository.findByUserAndChannel(user.get(), channel.get());
            if(userChannel.isPresent()) {
               userChannelRepository.delete(userChannel.get());
               isDeleted = true;
            }
        }

        return isDeleted;
    }

    @Override
    public List<String> getChannelMember(String channelId) {
        Optional<Channel> channel = channelRepository.findByChannelId(channelId);
        List<String> memberNickList = new ArrayList<>();
        if(channel.isPresent()) {
            Optional<List<UserChannel>> userChannels = userChannelRepository.findAllByChannel(channel.get());
            if(userChannels.isPresent()) {
                for (UserChannel userChannel: userChannels.get()) {
                    memberNickList.add(userChannel.getUser().getNickname());
                }
            }
        }

        return memberNickList;
    }
}
